import AjaxApi from './AjaxApi.js';

const DraftApi = {
  getAll: function (callback) {
    AjaxApi.post('/api/draft/all', AjaxApi.body(JSON.parse(sessionStorage.getItem('user')), {})
    ).then(response => {
      callback(response.status === 200 ? response.body : null);
    })
  },
  remove: function (draft, callback) {
    AjaxApi.post('/api/draft/remove', AjaxApi.body(draft, {})
    ).then(response => {
      callback(response.status === 200 ? response.body : null);
    })
  }
}

export default DraftApi;