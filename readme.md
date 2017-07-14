https://github.com/zl736732419/disruptor-study

https://github.com/mikeb01/disruptor-benchmarks

https://github.com/anair-it/disruptor-spring-manager

### 1.核心类和接口
    EventHandler：用户提供具体的实现，在里面实现事件的处理逻辑。
    Sequence：代表事件序号或一个指向缓存某个位置的序号。
    WaitStrategy：功能包括：当没有可消费的事件时，根据特定的实现进行等待，有可消费事件时返回可事件序号；有新事件发布时通知等待的 SequenceBarrier。
    Sequencer：生产者用于访问缓存的控制器，它持有消费者序号的引用；新事件发布后通过WaitStrategy 通知正在等待的SequenceBarrier。
    SequenceBarrier：消费者关卡。消费者用于访问缓存的控制器，每个访问控制器还持有前置访问控制器的引用，用于维持正确的事件处理顺序；通过WaitStrategy获取可消费事件序号。
    EventProcessor：事件处理器，是可执行单元，运行在指定的Executor里；它会不断地通过SequenceBarrier获取可消费事件，当有可消费事件时调用用户提供的 EventHandler实现处理事件。
    EventTranslator：事件转换器，由于Disruptor只会覆盖缓存，需要通过此接口的实现来更新缓存里的事件来覆盖旧事件。
    RingBuffer：基于数组的缓存实现，它内部持有对Executor、WaitStrategy、生产者和消费者访问控制器的引用。
    Disruptor：提供了对 RingBuffer 的封装，并提供了一些DSL风格的方法，方便使用。
### 2.功能
#### 2.1.发布数据
    1.RingBuffer.next -> Sequencer.next 这里可能会block住取决于消费端的消费速度。
    2.RingBuffer.publish -> Sequencer.publish -> WaitStrategy.signalAllWhenBlocking 如果消费者在这里block住了，会通知消费者消费。
#### 2.2.消费数据
    EventProcessor->SequenceBarrier.waitFor->EventHandler.onEvent如果消费速度赶上生产速度SequenceBarrier会把消费流程block住。
    Disruptor提供了两种预设的消费者，BatchEventProcessor，WorkProcessor，
    区别在于BatchEventProcessor是批量读取消费，协同工作的方式是使用多个BatchEventProcessor一起消费，这种情况下每个BatchEventProcessor都是消费相同的数据，但是可以在它里面通过hash取模的方式过滤掉应该被其它线程拉取的数据
    而WorkProcessor是单条消费一组WorkProcessor组成一个WorkerPool共享一个消费Sequence实现协同工作,这种工作方式因为有多个线程共享的Sequence所以违反了单写原则是有一定性能损耗的，
#### 2.3.多级消费
```
// 方法1
disruptor.handleEventsWithWorkerPool(parserWorkPool).then(new ThenHandler());
// 方法2
SequenceBarrier sequenceBarrier = disruptorMsgBuffer.newBarrier();
batchParseProcessors[i] = new BatchEventProcessor (disruptorMsgBuffer,sequenceBarrier,handlers[i])
```
### 3.参数配置刨析
#### 3.1.事件工厂
    从名字上理解就是"事件工厂"，其实它的职责就是产生数据填充RingBuffer的区块 。通过 EventFactory 在 RingBuffer 中预创建 Event 的实例，一个Event实例实际上被用作一个“数据槽”，
    发布者发布前，先从RingBuffer获得一个Event的实例，然后往Event实例中填充数据，之后再发布到RingBuffer中，之后由Consumer获得该Event实例并从中读取数据。
    需要实现接口 com.lmax.disruptor.EventFactory<T>
#### 3.2.ringBufferSize
    大小必须是2的N 次方；目的是为了将求棋运算转为&运算提高效率
#### 3.3.等待策略
    RingBuffer的生产都在没有可用区块（slot）的时候（可能是消费者(或者说是事件处理器)太慢了）的等待策略，定义了WaitStrategy 接口用于抽象 Consumer 如何等待新事件，根据实际运行环境的 CPU 的硬件特点选择恰当的策略，并配合特定的 JVM 的配置参数，能够实现不同的性能提升。
    BlockingWaitStrategy 是最低效的策略，但其对CPU的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现；
    SleepingWaitStrategy 的性能表现跟 BlockingWaitStrategy 差不多，对 CPU 的消耗也类似，但其对生产者线程的影响最小，适合用于异步日志类似的场景；
    YieldingWaitStrategy 的性能是最好的，适合用于低延迟的系统。在要求极高性能且事件处理线数小于 CPU 逻辑核心数的场景中，推荐使用此策略
### 4.handleEvents和handleEventsWithWorkPool区别
##### 4.1.handleEvents
    采用hanldeEvents处理多个任务时，如果注册了多个任务处理器，那么这些任务处理器会一次执行,
    当然这些任务处理器要属于同一个工作组,disruptor.handleEventsWith(processors);
#### 4.2.handleEventsWithWorkPool
    采用handleEventsWithWorkPool处理多个任务时，如果注册了多个任务处理器，那么这些disruptor会从分组中的这些处理器中选择其中一个执行，
    而不是像上面handleEvents那样每一个处理器都一次执行,（当然这些任务处理器要属于同一个工作组）disruptor.handleEventsWithWorkPool(processors);

>disruptor框架中多个线程之间数据的传递是通过定义的事件绑定数据传递的

#### 测试用例
    该项目模拟学生考试过程，从做选择题、填空题、解答题等题型，这里的答题顺序为选择题->填空题->解答题->考试结束