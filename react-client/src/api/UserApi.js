import AjaxApi from './AjaxApi.js';

const UserApi = {
  users: [],
  login: function (user) {
    let data = AjaxApi.body();
    data.body = user;
    console.log(AjaxApi.post('/login', data));
    return this.users.indexOf(user) !== -1;
  },
  register: function (user) {
    this.users.push(user);
  }
}

export default UserApi;