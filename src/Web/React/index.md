## Components

### Class Component
```javascript
import React from 'react';

class Greeting extends React.Component {
  render() {
    return <h1>Hello, {this.props.name}!</h1>;
  }
}

```

### Props and State
```javascript
import React from 'react';

class Counter extends React.Component {
  constructor(props) {
    super(props);
    this.state = { count: 0 };
  }

  render() {
    return (
      <div>
        <h1>Count: {this.state.count}</h1>
        <button onClick={() => this.setState({ count: this.state.count + 1 })}>Increment</button>
      </div>
    );
  }
}

export default Counter;
```

### Handling event
```javascript
import React from 'react';

class MyButton extends React.Component {
  handleClick(arg1, arg2) {
    console.log('Button clicked with arguments:', arg1, arg2);
  }

  render() {
    return (
      <button onClick={() => this.handleClick('foo', 'bar')}>Click me</button>
    );
  }
}

```

### Conditional Rendering
```javascript
class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showComponent: true
    };
  }

  handleClick = () => {
    this.setState({
      showComponent: !this.state.showComponent
    });
  }

  render() {
    return (
      <div>
        <button onClick={this.handleClick}>Toggle Component</button>
        {this.state.showComponent ? <MyComponent /> : null}
      </div>
    );
  }
}
```

```javascript
class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = { isLoggedIn: false };
  }

  render() {
    return (
      <div>
        {this.state.isLoggedIn ? (
          <div>
            <h1>Welcome back!</h1>
            <button onClick={() => this.setState({ isLoggedIn: false })}>Log out</button>
          </div>
        ) : (
          <div>
            <h1>Please log in</h1>
            <button onClick={() => this.setState({ isLoggedIn: true })}>Log in</button>
          </div>
        )}
      </div>
    );
  }
}
```

```javascript
class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = { isLoggedIn: false };
  }

  render() {
    if (this.state.isLoggedIn) {
      return (
        <div>
          <h1>Welcome back!</h1>
          <button onClick={() => this.setState({ isLoggedIn: false })}>Log out</button>
        </div>
      );
    } else {
      return (
        <div>
          <h1>Please log in</h1>
          <button onClick={() => this.setState({ isLoggedIn: true })}>Log in</button>
        </div>
      );
    }
  }
}
```

```javascript
function MyComponent(props) {
  return (
    <div>
      {props.title && <h1>{props.title}</h1>}
      <p>Some content here</p>
    </div>
  );
}
```

```javascript
function MyComponent(props) {
  const style = {
    display: props.isVisible ? 'block' : 'none',
    color: props.isHighlighted ? 'red' : 'black'
  };

  return (
    <div style={style}>
      <p>Some content here</p>
    </div>
  );
}
```

```javascript
return (
  <div>
    {isLoggedIn ? <LogoutButton /> : <LoginButton />}
  </div>
);

return (
  <div>
    {showComponent && <MyComponent />}
  </div>
);

```
