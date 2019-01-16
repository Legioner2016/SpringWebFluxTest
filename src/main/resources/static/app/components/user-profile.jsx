import React, { Component }  from 'react';
import { connect } from 'react-redux';

class UserProfile extends Component {
  render() {
    return (
      <div className="list-group user-profile">
        <div className="list-group-item">
          <p className="text-center">{this.props.user.alias}</p>
        </div>
      </div>
    );
  }
}

export default connect(({user}) => ({user}))(UserProfile);
