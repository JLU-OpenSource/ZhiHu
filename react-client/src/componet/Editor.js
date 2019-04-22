import React from 'react'
import moment from "moment";
import BraftEditor from 'braft-editor'
import { notification, Typography, Modal, Radio, Input, Form, message } from "antd";
import Table from 'braft-extensions/dist/table'
import Markdown from 'braft-extensions/dist/markdown'
import CodeHighlighter from 'braft-extensions/dist/code-highlighter'
import 'braft-editor/dist/index.css'
import 'braft-extensions/dist/table.css'
import 'braft-extensions/dist/code-highlighter.css'
import 'prismjs/components/prism-java'
import 'prismjs/components/prism-php'

import EditorApi from '../api/EditorApi.js';
import AjaxApi from '../api/AjaxApi.js';

const options = {
  syntaxs: [
    { name: 'JavaScript', syntax: 'javascript' },
    { name: 'HTML', syntax: 'html' },
    { name: 'CSS', syntax: 'css' },
    { name: 'Java', syntax: 'java', },
    { name: 'PHP', syntax: 'php' }
  ]
}

const RadioGroup = Radio.Group;

BraftEditor.use(CodeHighlighter(options))

BraftEditor.use(Table({
  defaultColumns: 5,
  defaultRows: 3,
  withDropdown: true
}))

BraftEditor.use(Markdown())

class Editor extends React.Component {

  state = {
    editorState: BraftEditor.createEditorState(null),
    options: this.props.options,
    modalVisible: false,
    contentType: 'question',
    contentTitle: '',
    confirmLoading: false,
    answerId: -1
  }

  async componentDidMount() {
    const type = this.state.options.type;
    const _this = this;
    if (type === 'article' || type === 'question') {
      notification['success']({
        key: 'tips',
        message: '快捷键支持！',
        description: '您可以使用Microsoft Word的常用快捷键，例如：按下 Control+S 或 Cmd+S 保存您当前创作的内容。',
        duration: 5,
      });
    } else if (type === 'answer') {
      notification['info']({
        key: 'tips',
        message: '正在撰写答案',
        description: '您正在为问题 "' + this.state.options.body.title + '" 撰写答案，按下 Control+S 或 Cmd+S 保存，或点击右上角发布按钮发布答案。',
        duration: 8,
      });
    } else if (type === 'draft') {
      notification['info']({
        key: 'tips',
        message: '正在恢复草稿',
        description: '继续编写 "' + this.state.options.body.title + '" ，按下 Control+S 或 Cmd+S 保存，或点击右上角发布按钮发布内容。',
        duration: 8,
      });
    }
    if (this.state.options.body != null) {
      this.setState({ contentType: this.state.options.type })
      switch (this.state.options.type) {
        case 'draft':
          fetch(AjaxApi.host + "/draft/" + this.state.options.body.id + ".html")
            .then(response => response.text())
            .then(text => this.setState({ editorState: BraftEditor.createEditorState(text) }))
          break;
        case 'answer':
          EditorApi.getMyAnswer(this.state.options.body.key, function callback(response) {
            if (response != null) {
              _this.setState({ answerId: response.aid })
              fetch(AjaxApi.host + "/answer/" + response.aid + ".html")
                .then(response => response.text())
                .then(text => _this.setState({ editorState: BraftEditor.createEditorState(text) }))
            }
          });
          break;
        default: break;
      }
    }
  }

  handleSubmitToServer = () => {
    if (this.state.contentType === 'answer') {
      const rawString = this.state.editorState.toRAW()
      const htmlString = this.state.editorState.toHTML()
      const answer = {
        'qid': this.state.options.body.key,
        'author': JSON.parse(sessionStorage.getItem('user')),
        'title': this.state.options.body.title
      }
      if (this.state.answerId !== -1) {
        answer['aid'] = this.state.answerId
      }
      EditorApi.createAnswer(answer, htmlString, rawString, function callback(response) {
        window.location.reload();
      })
    } else
      this.setState({ modalVisible: true })
  }

  handleEditorChange = (editorState) => {
    this.setState({ editorState })
  }

  handleSave = () => {
    const rawString = this.state.editorState.toRAW()
    const htmlString = this.state.editorState.toHTML()
    const _this = this;
    let drfat;
    if (this.state.contentType !== 'draft')
      drfat = {
        'title': '未命名 ' + moment().format('YYYY-MM-DD HH:mm:ss'),
        'author': JSON.parse(sessionStorage.getItem('user'))
      };
    else
      drfat = this.state.options.body;
    EditorApi.saveDraft(drfat, htmlString, rawString, function callback(response) {
      _this.setState({
        confirmLoading: false, modalVisible: false,
        contentType: 'draft', options: {
          type: 'draft', body: response
        }
      })
      message.success(' "' + response.title + '" 已经保存');
    })
  }

  handleSubmit = () => {
    if (this.state.contentTitle.length === 0 ||
      this.state.contentTitle.trim().length === 0
      || this.state.contentTitle.length > 25)
      message.error("标题不能为空并且不超过25个字符");
    else {
      const rawString = this.state.editorState.toRAW()
      const htmlString = this.state.editorState.toHTML()
      const _this = this;
      _this.setState({ confirmLoading: true })
      if (_this.state.contentType === 'question') {
        const question = {
          'title': _this.state.contentTitle,
          'author': JSON.parse(sessionStorage.getItem('user'))
        }
        EditorApi.createQuestion(question, htmlString, rawString, function callback(response) {
          _this.setState({ confirmLoading: false, modalVisible: false })
          message.success('问题已发布');
          _this.props.onSubmitQuestion();
        })
      } else if (_this.state.contentType === 'article') {
        const article = {
          'title': _this.state.contentTitle,
          'author': JSON.parse(sessionStorage.getItem('user'))
        }
        EditorApi.createArticle(article, htmlString, rawString, function callback(response) {
          _this.setState({ confirmLoading: false, modalVisible: false })
          message.success('文章已发布');
          _this.props.onSubmitArticle(response);
        })
      }
    }
  }

  handleCancel = () => {
    this.setState({ modalVisible: false })
  }

  onRadioChange = (e) => {
    this.setState({
      contentType: e.target.value,
    });
  }

  handleTitleChange = (e) => {
    this.setState({ contentTitle: e.target.value })
  }

  render() {
    const extendControls = [
      {
        key: 'submit',
        type: 'button',
        text: '发布',
        onClick: this.handleSubmitToServer
      }]
    const { editorState } = this.state
    return (
      <Typography>
        <BraftEditor
          onChange={this.handleEditorChange}
          onSave={this.handleSave}
          extendControls={extendControls}
          value={editorState} />
        <Modal
          title="发布"
          visible={this.state.modalVisible}
          onOk={this.handleSubmit}
          onCancel={this.handleCancel}
          confirmLoading={this.state.confirmLoading}
        >
          <Form>
            <Form.Item>
              <RadioGroup onChange={this.onRadioChange} value={this.state.contentType}>
                <Radio value='question'>发布问题</Radio>
                <Radio value='article'>发表文章</Radio>
              </RadioGroup>
            </Form.Item>
            <Input placeholder='输入标题' onChange={this.handleTitleChange} />
          </Form>
        </Modal>
      </Typography>
    )
  }
}

export default Editor;