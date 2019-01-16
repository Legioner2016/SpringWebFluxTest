import { combineReducers } from 'redux';
import MessagesReducer from './messages';
import UserReducer from './user';
import UserStatsReducer from './stats';

const rootReducer = combineReducers({
  messages: MessagesReducer,
  user: UserReducer,
  stats: UserStatsReducer
});

export default rootReducer;