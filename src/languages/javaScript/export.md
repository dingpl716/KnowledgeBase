### As Constructor:
```javascript
// 申明部分
function person(name) {
  this.name = name;
}
// 不能写成 exports = person
module.exports = person;
```

```javascript
// 引用部分
const Person = require('./l1');
const person = new Person('Peiling');
console.log(person.name);
```

### 在一个js文件里面export多个class
```javascript
// 定义多个class
class Block {
  constructor(hash, … ) {
      this.hash = hash;
  }
}
class Transaction {
  constructor(block, …) {
    this.blk_hash = block.hash;
  }
}
module.exports = { Block, Transaction }
```

```javascript
// 引用
const { Block, Transaction } = require('./models');
```

### As function:
```javascript
// 申明
function person(name) {
  console.log(name);
}
// 在exports后面必须加.person
exports.person = person;
```

```javascript
// 第一种引用方法
// 必须在require后面加.person
const person = require('./l2').person;
person('Peiling');
```

```javascript
// 第二种引用方法
const { person } = require('./l2');
person('Peiling');
```