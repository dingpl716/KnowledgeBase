# Smart Pointers (智能指针)
- **在 Rust 中，凡是需要做资源回收的数据结构，且实现了 Deref/DerefMut/Drop，都是智能指针**
- 一个只能指针一定是一个胖指针，但是反过来却不一定。

- `Rc`，`RefCell`搭配使用，在**不需要**多线程访问的情况下，实现对一个数据的多个所有者，其内部实现**不是**线程安全的
- `Arc`和`Mutex`, `RwLock`搭配使用，在需要多线程访问的情况下，实现对一个数据的多个所有者，其内部实现是线程安全的
- `Box<T>`，用来在堆上分配内存，它是Rust中最基本的在堆上分配内存的方式，绝大多数其它包含堆内存分配的数据类型，内部都是通过 Box 完成的，比如 `Vec<T>`。它相当于C里面的`malloc`
- `Cow<'a, B>`
- `PathBuf`
- `MutexGuard<T>`
- `RwLockReadGuard<T>`
- `RwLockWriteGuard<T>`

## Rc (Reference counter) & RefCell

- `Rc`用来用来计数，使一个值可以拥有多个所有者。
- 对一个 Rc 结构进行 clone()，不会将其内部的数据复制，只会增加引用计数。
- 而当一个 Rc 结构离开作用域被 drop() 时，也只会减少其引用计数，直到引用计数为零，才会真正清除对应的内存。
- Rc 是一个只读的引用计数器，你无法拿到 Rc 结构内部数据的可变引用，来修改这个数据。

### Example 1

```rust
use std::rc::Rc;
fn main() {
    let a = Rc::new(1);
    let b = a.clone();
    let c = a.clone();
}
```

### Example 2, 实现一个DAG，有向无环图

```rust
use std::rc::Rc;

#[derive(Debug)]
struct Node {
    id: usize,
    downstream: Option<Rc<Node>>,
}

impl Node {
    pub fn new(id: usize) -> Self {
        Self {
            id,
            downstream: None,
        }
    }

    pub fn update_downstream(&mut self, downstream: Rc<Node>) {
        self.downstream = Some(downstream);
    }

    pub fn get_downstream(&self) -> Option<Rc<Node>> {
        self.downstream.as_ref().map(|v| v.clone())
    }
}

fn main() {
// 从上往下的DAG 
// 1     2
//  \   /
//    3
//    |
//    4

    let mut node1 = Node::new(1);
    let mut node2 = Node::new(2);
    let mut node3 = Node::new(3);
    let node4 = Node::new(4);
    node3.update_downstream(Rc::new(node4));

    node1.update_downstream(Rc::new(node3));
    node2.update_downstream(node1.get_downstream().unwrap());
    println!("node1: {:?}, node2: {:?}", node1, node2);


    // 这部分代码无法编译，node3 cannot borrow as mutable
    let node5 = Node::new(5);
    let node3 = node1.get_downstream().unwrap();
    node3.update_downstream(Rc::new(node5));

    println!("node1: {:?}, node2: {:?}", node1, node2);
}
```

- 上面的代码表达出我们想修改这个图，所以针对上面的编译错误我们需要使用`RefCell`，它对某个只读数据进行可变借用

### Example 3
```rust

use std::cell::RefCell;

fn main() {
    let data = RefCell::new(1);
    // 这个花括号是必须的，否则编译时正确，但运行时出错
    // 这是因为不能同时有活跃的可变借用和不可变借用
    // 这个花括号明确的缩小了可变借用的生命周期
    // 这里之所以只在运行时报错，是因为内部可变性
    {
        // 获得 RefCell 内部数据的可变借用
        let mut v = data.borrow_mut();
        *v += 1;
    }
    // 打印结果是 2
    println!("data: {:?}", data.borrow());
}
```

|                                | 使用方法            | 所有权检查                   |
| -------------------------------| ------------------- | --------------------------|
| 外部可变性(exterior mutability)  | `let mut` or `&mut` | 编译时，如果不符合规则，报错  |
| 内部可变性（interior mutability) | `Cell / RefCell`    | 运行时，如果不符合规则，panic |

### Example 4，可修改的DAG

```rust

use std::cell::RefCell;
use std::rc::Rc;

#[derive(Debug)]
struct Node {
    id: usize,
    // 使用 Rc<RefCell<T>> 让节点可以被修改
    downstream: Option<Rc<RefCell<Node>>>,
}

impl Node {
    pub fn new(id: usize) -> Self {
        Self {
            id,
            downstream: None,
        }
    }

    pub fn update_downstream(&mut self, downstream: Rc<RefCell<Node>>) {
        self.downstream = Some(downstream);
    }

    pub fn get_downstream(&self) -> Option<Rc<RefCell<Node>>> {
        self.downstream.as_ref().map(|v| v.clone())
    }
}

fn main() {
    let mut node1 = Node::new(1);
    let mut node2 = Node::new(2);
    let mut node3 = Node::new(3);
    let node4 = Node::new(4);

    node3.update_downstream(Rc::new(RefCell::new(node4)));
    node1.update_downstream(Rc::new(RefCell::new(node3)));
    node2.update_downstream(node1.get_downstream().unwrap());
    println!("node1: {:?}, node2: {:?}", node1, node2);

    let node5 = Node::new(5);
    let node3 = node1.get_downstream().unwrap();
    // 获得可变引用，来修改 downstream
    node3.borrow_mut().downstream = Some(Rc::new(RefCell::new(node5)));

    println!("node1: {:?}, node2: {:?}", node1, node2);
}

```

```rust
        // This is the traversal that fails to compile.
        let mut curr = self.head.as_ref().unwrap();
        for _ in 1..idx {
            curr = curr.borrow().next.as_ref().unwrap()
        }

        let mut curr = self.head.as_ref().unwrap().clone();
        for _ in 1..idx {
            let t = curr.borrow().next.as_ref().unwrap().clone();
            curr = t;
        }
```

## Arc (Atomic reference counter)

## Box<T>


