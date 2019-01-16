import React from 'react';
// import Rx from 'rxjs/Rx';
import { joinChat } from '../actions/chat';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';

class Login extends React.Component {

  static contextTypes = {
    router: PropTypes.object
  }

  constructor(props){
    super(props);
    this.state = {alias: ''}
  }

  onAliasChange(alias){
    this.setState({alias});
  }

  onSubmit(e) {
    e.preventDefault();
    const {alias} = this.state;
    this.props.joinChat({alias});
    this.context.router.history.push('/chat');
    return false;
  }

  componentDidMount(){
    // this.aliasInput &&
    //   Rx.Observable.
    //   fromEvent(this.aliasInput,'keyup').
    //   map(e => e.target.value).
    //   distinctUntilChanged().
    //   debounceTime(500)
  }

  render() {
    return (
      <div className="container">
        <div className="panel panel-default card card-container">
          <p id="profile-name" className="profile-name-card"></p>
          <form className="form" onSubmit={this.onSubmit.bind(this)}>
            <div className="form-group">
              <input type="text"
                id="inputAlias"
                value={this.state.alias}
                className="form-control input-lg"
                placeholder="Alias"
                ref={input => this.aliasInput = input}
                onChange={event => this.onAliasChange(event.target.value)}
                required autoFocus/>
            </div>
            <div className="form-group">
              <button className="btn btn-lg btn-success btn-block" type="submit">Chat</button>
            </div>
          </form>
        </div>
      </div>
    );
  }
}
export default connect(null,{ joinChat })(Login);