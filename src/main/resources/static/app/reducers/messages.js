import {MESSAGE_RECEIVED} from '../actions/chat';
const initialState = [];
export default function(state = initialState, action){
  switch(action.type){
      case MESSAGE_RECEIVED:
        var temp = state.slice();
        temp.push(action.payload); 
        return temp;
      default: return state;
  }
}