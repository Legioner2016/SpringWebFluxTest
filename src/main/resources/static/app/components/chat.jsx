import React from 'react';
import Nav from './nav.jsx';
import OnlineUsers from './online-users.jsx';
import Messages from './messages.jsx';
import MessageInput from './message-input.jsx';
import UserProfile from './user-profile.jsx';

export default class Chat extends React.Component {
  render() {
    return (
      <div className="full-height">
        <div className="row">
          <Nav/>
        </div>
        <div className="row full-height">
          <div className="col-md-3 full-height">
            <UserProfile/>
            {/* <OnlineUsers/> */}
          </div>
          <div className="col-md-9 full-height">
            <div className="full-height">
              <Messages/>
              <MessageInput/>
            </div>
          </div>
        </div>
      </div>
    );
  }
}