import AjaxApi from './AjaxApi.js';

const UserApi = {
  login: function (user, callback) {
    AjaxApi.post('/user/login', AjaxApi.body(user, {})
    ).then(response => {
      if (response.status === 200) {
        sessionStorage.setItem('token', response.body);
        sessionStorage.setItem('email', user.email);
      }
      callback(response.status === 200)
    })
  },
  register: function (user, callback) {
    AjaxApi.post('/user/register', AjaxApi.body(user, {})
    ).then(response => {
      if (response.status === 200) {
        sessionStorage.setItem('token', response.body);
        sessionStorage.setItem('email', user.email);
      }
      callback(response.status === 200)
    })
  }
}

export default UserApi;