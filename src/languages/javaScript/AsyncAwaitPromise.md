## 基本概念
- JS Runtime中有Call Stack，Macro Task Queue和Micro Task Queue
- Call Stack上的程序是Synchronous的，而两个Task Queue里面的是Async的
- Call Stack的执行优先级高于Task Queu，即当Call Stack上有程序要执行时，Task Queu里面的tasks就必须等待。
- 每次Event Loop的执行顺序可以理解为如下：
  When call stack is empty, do the steps
  1. Select the oldest task(task A) in macrotask queues
  2. If task A is null(means macrotask queues is empty), jump to step 6
  3. Set "currently running task" to "task A"
  4. Run "task A"(means run the callback function)
  5. Set "currently running task" to null,remove "task A"
  6. Perform microtask queue
      - (a) select the oldest task(task x) in microtask queue
      - (b) if task x is null(means microtask queues is empty),jump to step (g)
      - (c) set "currently running task" to "task x"
      - (d) run "task x"
      - (e) set "currently running task" to null,remove "task x"
      - (f) select next oldest task in microtask queue,jump to step(b)
      - (g) finish microtask queue;
  7. Jump to step 1.
- 在上面的描述中需要注意以下几点：
  - 执行一个Task，是需要将他push到Call Stack上执行的
  - 在执行一个Task的时候，会产生新的task，如果产生了新的Microtask，那么会在**本轮**Event Loop中将之执行完成，但如果产生的是Macrotask，那么会在**下一次**Event Loop的时候执行。
  - Resolve或者Reject的Promise会在本轮Event Loop执行，而Pending的Promise会在之后执行。
  - **一段JS代码的主程序本身也是一个Macrotask，并且在第一次Event Loop的时候执行**。

### MicroTask v.s. MacroTask
- MacroTask will be executed on the next event loop, including `setTimeout`, `setInterval`, `setImmediate`, `requestAnimationFrame`, `I/O`, `UI rendering`
- MicroTask will be called back before the start of the next event loop, including `process.nextTick`, `Promises`, `queueMicrotask`, `MutationObserver`

#### 例 1：
```javascript
console.log('Synchronous 1');

setTimeout(_ => console.log('Timeout 2'), 0);

Promise.resolve().then(_ => console.log('Promise 3'));

console.log('Synchronous 4');
```

执行结果
```
Synchronous 1  
Synchronous 4
Promise 3        # Promise 3 先于 Timout 2出现，是因为Promise这个MicroTask是在下一次event loop之
Timeout 2        # 前被调用， 而setTimeout这个MacroTask是在下一次loop时调用。
```

### Catch Error

- 可以用一个`catch`来处理整个async链上的所有错误
- 如果链上某一个地方出错了，那么会直接跳到`catch`而忽略其他的步骤。
- 在下例中`user.title`不会被log

```javascript
promise
  .then(res => res.json())
  .then(user => {
    throw new Error('Something bad happen.')
    return user;
  })
  .then(user => console.log(user.title))
  .catch(err => console.log(err));
```

### Promise.Then里Callback的执行顺序

#### 例 1：
```javascript
const tick = Date.now();
const log = (v) => console.log(`${v} \n Elapsed: ${Date.now() - tick}ms`);

const codeBlocker = () => {
  let i = 0;
  while (i < 1000000000) {
    ++i;
  }

  return 'Billions of loops are done.';
}

log('Synchronous 1');
log(codeBlocker());
log('Synchronous 2');
```

执行结果
```
Synchronous 1
 Elapsed: 0ms
Billions of loops are done.  # 因为while loop是在call stack上执行的，所以它block住了后面的语句
 Elapsed: 923ms              # for循环也一样是在call statck上执行的
Synchronous 2
 Elapsed: 924ms
```

#### 例 2: 
```javascript
const tick = Date.now();
const log = (v) => console.log(`${v} \n Elapsed: ${Date.now() - tick}ms`);

const codeBlocker = () => {
  return new Promise((resolve, reject) => {
    let i = 0;
    while (i < 1000000000) {
      ++i;
    }
    resolve('Billions of loops are done.');
  });
}

log('Synchronous 1');
codeBlocker().then(log);
// codeBlocker().then(val => log(val));  上以行也可以写成这样
log('Synchronous 2');
```

执行结果
```
Synchronous 1
 Elapsed: 0ms
Synchronous 2
 Elapsed: 867ms
Billions of loops are done.
 Elapsed: 868ms
```

解释
- 在上面的执行结果中，我们任然等待了867ms才打印出`Synchronous 2`。这说明`while`循环仍然是在Call Stack上synchronously执行的，而不是在Task Queue里面async执行的。
- 但是，与例一不同，`Synchronous 2`是紧随`Synchronous 1`被打印出来。这就说明`.then(log)`是在最后执行的。
- 由此可见，这段代码的执行顺序应当是：
  1. `log('Synchronous 1')`
  2. `codeBlocker()`         <--- 在`new Promise`中执行`while`循环，并且通过`.then(log)`将回调函数enqueue
  3. `log('Synchronous 2')`
  4. `log`            <--- 将Promise里面的返回值log起来

#### 例 3：

```javascript
const tick = Date.now();
const log = (v) => console.log(`${v} \n Elapsed: ${Date.now() - tick}ms`);

const codeBlocker = () => {
  return Promise.resolve().then(_ => {
    let i = 0;
    while (i < 1000000000) {
      ++i;
    }
    return 'Billions of loops are done.';
  });
}

log('Synchronous 1');
codeBlocker().then(log);
log('Synchronous 2');
```

执行结果

```
Synchronous 1
 Elapsed: 0ms
Synchronous 2
 Elapsed: 2ms
Billions of loops are done.
 Elapsed: 891ms
```

解释

- `Synchronous 1`和`Synchronous 2`相继出现，并且只间隔2ms，说明`codeBlocker`终于没有在Call Stack上执行了
- 由此可以，**只有在`.then()`里面的callback**才是真正在Task Queue里面执行的。

### 顺序执行每个Then里的Callback

#### 例 1
```javascript
function test() {
  return Promise.resolve()
    .then(_ => {
      console.log('1.1');
      setTimeout(() => {
        console.log('1.5')
      }, 0);
      Promise.resolve().then(_ => console.log('1.3'));
      console.log('1.2');
      Promise.resolve().then(_ => console.log('1.4'));
    })
    .then(_ => {
      console.log('2.1');
      return Promise.resolve().then(_ => console.log('2.2'));
    });
}

test();
```

执行结果
```
1.1
1.2
1.3
1.4
2.1
2.2
1.5
```

解释

- 因为该例中的Promise都已经resolve，因此需要在本轮Event Loop里执行所有callback，即`1.x`和`2.x`，除`1.5`以外。
- 因为`2.x`整体落后于`1.x`(除`1.5`以外)，由此可知，后一个Callback必须等待前一个Callback完全执行完毕才能执行。
- 并且，如果一个Callback里面嵌套了更多的callback，那么要等这些callback全部执行完毕，才能称之为完全执行完毕，就像第一个callback里面嵌套了`1.3`和`1.4`
- 因为`1.2`先于`1.3`, `1.4`出现，所以可以得知，在处理一个callback的时候，JS仍然遵循先执行Synchronous的代码，然后执行Async的代码
- 如果我们把这个代码块看成一个树的话，那么callback相当于是树上的节点，而JS将按照DFS的顺序执行这些节点里的代码。
- 并且对于每一个节点，JS都是先执行同步的代码，然后在执行异步的代码
- **疑问???** 如果要以DFS的顺序执行回调函数，那么这个Task Queue到底是怎样的一个结构呢？
  - 假设1：这个Task Queue不是简单的在最末尾添加一个Task，而是可以将子回调函数插入到父回调函数的后面，如果用一个链表的话，也可以做到O(1)的时间复杂度。
  - 假设2：这个Task Queue是一个堆或者Priority Queue，并且子回调函数的顺序小于父回调函数的兄弟，这样可以在O(logn)的时间内完成入队操作。
  - 假设3：这个Task Queue也是一有个层级结构的，并且会将子回调函数插入到一个不同于父回调函数的Queue里面，并且优先执行子Queue里面的Task。
- 在此例中，我们只嵌套了一层，但是嵌套多层仍然会是顺序的执行每个回调函数。
- `1.5`是一个MacroTask，它会在下一次Event Loop的时候被执行，所以最后出现。

### Async Await

- `async` `await`的初衷是提供一个语法糖，让上面那种callback链的代码更容易阅读
- 所以`async`的第一个主要作用就是把一个函数的返回值包在一个Promise里面，所以我们也可以用`.then`的方法来使用一个`async`函数的返回值
- `async`的第二个作用是允许我们在一个函数中使用`await`，具体来讲，只能在`async`的函数里面使用`await`，被`await`的另一个函数要么是`async`的，要么返回一个`Promise`
- 一个`async`的函数既可以通过`await`来调用，又可以通过`.then`来调用。

#### 语法上互换
```javascript
const getDataAsync = async (key) => {
  const data = { a: 'a', b: 'b', c: 'c' }
  return data[key];
}

const getDataPromise = (key) => {
  const data = { a: 'a', b: 'b', c: 'c' }
  return Promise.resolve(data[key]);
}

async function testAsync() {
  getDataAsync('a').then(d => console.log(`getDataAsync then ${d}`));
  let d1 = await getDataAsync('a');
  console.log(`getDataAsync await ${d1}`);

  getDataPromise('a').then(d => console.log(`getDataPromise then ${d}`));
  let d2 = await getDataPromise('a');
  console.log(`getDataPromise await ${d2}`);
}

testAsync();
```

执行结果
```
getDataAsync then a
getDataAsync await a
getDataPromise then a
getDataPromise await a
```

解释：
  - 由此可见，`async await`和`Promise`在语法上是可以互相转化且通用的

#### Await即同步，Then即异步：
```javascript
const getDataAsync = async (key) => {
  const data = { a: 'a', b: 'b', c: 'c' }
  return data[key];
}

async function testAsync() {
  console.log("Synchronous 1");
  console.log(`getDataAsync await ${await getDataAsync('a')}`);
  console.log("Synchronous 2");
  console.log(`getDataPromise await ${await getDataPromise('b')}`);
  console.log("Synchronous 3");
  getDataAsync('c').then(d => console.log(`getDataAsync then ${d}`));
  console.log("Synchronous 4");
  getDataPromise('a').then(d => console.log(`getDataPromise then ${d}`));
}

testAsync();
```

执行结果
```
Synchronous 1
getDataAsync await a
Synchronous 2
getDataPromise await b
Synchronous 3
Synchronous 4
getDataAsync then c
getDataPromise then a
```

解释
  - 由于`Synchronous 1`, `getDataAsync await a`, `Synchronous 2`, `getDataPromise await b`, `Synchronous 3`依次出现，可以得知，无论`await`的是一个`async`函数还是一个`Promise`, `await`关键字都将异步函数变成了**同步**函数。
  - 由于`Synchronous 3`, `Synchronous 4`先于`getDataAsync then c`和`getDataPromise then a`出现，所以可以得知，无论是一个`async`函数还是一个`Promise`，只要是放到`.then()`里面执行的，就一定是一个**异步**函数。

#### Await的本质是清空Task Queue
```javascript
const getDataAsync = async (key) => {
  const data = { a: 'a', b: 'b', c: 'c' }
  return data[key];
}

const getDataPromise = (key) => {
  const data = { a: 'a', b: 'b', c: 'c' }
  return Promise.resolve(data[key]);
}

async function testAsync() {
  console.log("Synchronous 1");
  getDataAsync('a').then(d => console.log(`getDataAsync then ${d}`));
  console.log("Synchronous 2");
  let d1 = await getDataAsync('a');
  console.log(`getDataAsync await ${d1}`);

  getDataPromise('a').then(d => console.log(`getDataPromise then ${d}`));
  console.log("Synchronous 3");
  let d2 = await getDataPromise('a');
  console.log(`getDataPromise await ${d2}`);
}

testAsync();
```

执行结果
```
Synchronous 1
Synchronous 2
getDataAsync then a
getDataAsync await a
Synchronous 3
getDataPromise then a
getDataPromise await a
```

解释
  - `await`的真正意义是**阻塞住主线程，并且清空Task Queue里面的所用异步Task**
  - 因此，代码的真正执行顺序是
    1. `console.log("Synchronous 1");` 同步执行，打印`"Synchronous 1"`
    2. `getDataAsync('a').then()`获取数据，并将`log`的task放入队列
    3. `console.log("Synchronous 2");` 同步执行，打印`"Synchronous 2"`
    4. `let d1 = await getDataAsync('a');` 清空队列里面的Task，所以会先打印出`getDataAsync then a`,因为这个任务在队列的前面，然后执行获取数据的逻辑
    5. `console.log(`getDataAsync await ${d1}`);` 同步执行，打印`getDataAsync await a`
    6. `getDataPromise('a').then();` 获取数据，并将`log`的task放入队列
    7. `console.log("Synchronous 3");` 同步执行，打印`"Synchronous 3"`
    8. `let d2 = await getDataPromise('a');` 清空队列里面的Task，所以会先打印出`getDataPromise then a`,因为这个任务在队列的前面，然后执行获取数据的逻辑
    9. `console.log(`getDataPromise await ${d2}`);` 同步执行，打印`getDataPromise await a`

#### 不要让Await不必要的阻塞住代码

```javascript
const getDataAsync = async (key) => {
  const data = { a: 'a', b: 'b', c: 'c' }
  return data[key];
}

const getAllData1 = async () => {
  const a = await getDataAsync('a');
  const b = await getDataAsync('b');

  return [a, b];
}

const getAllData2 = () => {
  const a = getDataAsync('a');
  const b = getDataAsync('b');

  return Promise.all([a, b]);
}

const getAllData3 = async () => {
  const a = getDataAsync('a');
  const b = getDataAsync('b');
  const all = await Promise.all([a, b]);

  return all;
} 

```

解释
  - 在`getAllData1`这段代码里，对数据`a`和`b`的获取实际上是同步执行的。也就是说，在获取到`a`之后，`b`才执行。但是在真实环境中，如果两个数据没有前后的依赖关系，我们其实可以并行的同时获取他们，而不应该这样阻塞住线程。
  - `getAllData2`和`getAllData3`等价

### 异步Interate

#### Array.map返回Promise数组
```javascript
const getDataAsync = async (key) => {
  const data = { a: 'a', b: 'b', c: 'c' }
  return data[key];
}

const keys = ['a', 'b', 'c'];
console.log('Synchronous 1');
const all = keys.map(key => getDataAsync(key));  // 这里的.map实际是返回了一个Promise数组
Promise.all(all).then(a => console.log(a));      // 所以在这个地方要用.all来resolve所有的promise
console.log('Synchronous 2');
```

执行结果
```
Synchronous 1
Synchronous 2
[ 'a', 'b', 'c' ]
```

#### Await在Array.map中 -- 异步入列，异步执行

```javascript
const fetch = require('node-fetch');
const tick = Date.now();
const log = (v) => console.log(`${v} \n Elapsed: ${Date.now() - tick}ms`);
const urls = ["https://google.com", "https://youtube.com", "https://facebook.com"];

const getDataAsync = async (url) => {
  const response = await fetch(url);
  return response;
}

const getDataAllMapAwait = () => {
  urls.map(async url => {
    log(`Before ${url}`);
    const data = await getDataAsync(url);
    log(data.url);
  });
}

log('Synchronous 1');
getDataAllMapAwait();
log('Synchronous 2');
```

执行结果
```
Synchronous 1
 Elapsed: 0ms
Before https://google.com
 Elapsed: 2ms
Before https://youtube.com
 Elapsed: 19ms
Before https://facebook.com
 Elapsed: 20ms
Synchronous 2
 Elapsed: 20ms
https://www.youtube.com/
 Elapsed: 231ms
https://www.google.com/
 Elapsed: 233ms
https://www.facebook.com/
 Elapsed: 375ms
```

解释
- `Synchronous 1`到`Synchronous 2`的部分是在Call Stack上执行的，也就是说`.map`循环里和`await`无关的部分都直接在Call Stack上执行了，但是`await`相关的部分都放入的Task Queue里面异步执行。
- 三个网址返回数据的时间很接近，都是在十几毫秒以内，所以可以知道，对三个网址的fetch操作是异步并行进行的，也就是说每个iteration之间是并行处理的。
- `.forEach`，`.filter`等API具有相似行为。

#### Await在for循环中 -- 异步入列，同步执行
```javascript
const fetch = require('node-fetch');
const tick = Date.now();
const log = (v) => console.log(`${v} \n Elapsed: ${Date.now() - tick}ms`);
const urls = ["https://google.com", "https://youtube.com", "https://facebook.com"];

const getDataAsync = async (url) => {
  const response = await fetch(url);
  return response;
}

const getDataAllForAwait = async () => {
  for (const url of urls) {
    // log(`Before ${url}`);
    const data = await getDataAsync(url);
    log(data.url);
  }
}

log('Synchronous 1');
getDataAllForAwait();
log('Synchronous 2');
```

执行结果
```
Synchronous 1
 Elapsed: 0ms
Synchronous 2
 Elapsed: 19ms
https://www.google.com/
 Elapsed: 232ms
https://www.youtube.com/
 Elapsed: 413ms
https://www.facebook.com/
 Elapsed: 692ms
```

解释
- 每个网址返回数据的时间都间隔几百毫秒，所以可以得知，在每次iteration的时候，`await`阻塞住了线程，使得数据的获取是同步执行的。也就是说，在本轮获取到数据之后才会进行下一轮的数据获取。
- 但是`Synchronous 1`和`Synchronous 2`相继出现，所以可以得知在这个`for`循环作为一个整体，并没有阻塞住主线程。也就是说，执行这个`for`循环的时候，任然是把所有iteration都先加入到Queue里面，但是当执行每个iteration的时候，又是以**阻塞的、同步的**形式执行的。
- `for...in..`，`for`和上例中的`for...of...`行为一致。
- 但是，倘若将上面注释掉的那一行取消注释，那么执行结果会变成：
  ```
  Synchronous 1
  Elapsed: 0ms
  Before https://google.com
  Elapsed: 2ms
  Synchronous 2
  Elapsed: 19ms
  https://www.google.com/
  Elapsed: 223ms
  Before https://youtube.com
  Elapsed: 224ms
  https://www.youtube.com/
  Elapsed: 411ms
  Before https://facebook.com
  Elapsed: 411ms
  https://www.facebook.com/
  Elapsed: 695ms
  ```
  对这个结果的解释是这样的：
  1. 首先在Call Stack上执行`log('Synchronous 1');`
  2. 然后在`for`循环的第一次iteration的时候， 在Call Stack上执行`log('Before https://google.com')`
  3. 紧接着遇到`await`，JS意识到需要将其加入到Task Queue里面，与前面`.map`不同的是，**此时JS会将`for`循环的执行暂停，并且等待数据返回，而不是继续执行`for`循环直到把所有`await`都加入队列**。
  4. 暂停`for`循环之后，在Call Stack上执行`log('Synchronous 2');`
  5. 数据返回了，执行`log`，打印出`https://www.google.com/`，并且我们知道已经过去223ms
  6. 然后顺序的、同步的执行剩下两次iteration.