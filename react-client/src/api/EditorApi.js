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
  }
}

export default EditorApi;