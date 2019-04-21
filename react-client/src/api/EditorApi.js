import AjaxApi from './AjaxApi.js';

const EditorApi = {
  saveDraft: function (draft, htmlContent, rawContent, callback) {
    AjaxApi.post('/api/draft/save', AjaxApi.body(draft, { 'html': htmlContent, 'raw': rawContent })
    ).then(response => {
      callback(response.status === 200 ? response.body : null);
    })
  },
  createQuestion: function (question, htmlContent, rawContent, callback) {
    AjaxApi.post('/api/question/create', AjaxApi.body(question, { 'html': htmlContent, 'raw': rawContent })
    ).then(response => {
      callback(response.status === 200 ? response.body : null);
    })
  },
  createAnswer: function (answer, htmlContent, rawContent, callback) {
    AjaxApi.post('/api/answer/create', AjaxApi.body(answer, { 'html': htmlContent, 'raw': rawContent })
    ).then(response => {
      callback(response.status === 200 ? response.body : null);
    })
  },
  createArticle: function (article, htmlContent, rawContent, callback) {
    AjaxApi.post('/api/article/create', AjaxApi.body(article, { 'html': htmlContent, 'raw': rawContent })
    ).then(response => {
      callback(response.status === 200 ? response.body : null);
    })
  },
  getMyAnswer: function (id, callback) {
    AjaxApi.post('/api/answer/author', AjaxApi.body(id, {})
    ).then(response => {
      callback(response.status === 200 ? response.body : null);
    })
  }
}

export default EditorApi;