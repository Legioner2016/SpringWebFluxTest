import React, {Component} from 'react';
import Login from './components/login.jsx';
import requireUser from './components/require_user.jsx';
import {connectToChatServer} from './actions/chat';
import {connect} from 'react-redux';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import Chat from './components/chat.jsx';

class App extends Component {

  componentDidMount(){
    this.props.connectToChatServer(`ws://${location.host}/websocket/chat`);
  }

  render(){
    return(
      <Router>
        <div className="full-height">
          <Route exact path="/" component={Login}/>
          <Route exact path="/chat" component={requireUser(Chat)}/> 
          {/* <Route exact path="/chat" component={Chat}/> */}
        </div>
      </Router>
    );
  }
}

export default connect(null, {connectToChatServer})(App);