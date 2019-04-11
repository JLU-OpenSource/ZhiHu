import React from 'react'
import BraftEditor from 'braft-editor'
import { notification, Typography } from "antd";
import Table from 'braft-extensions/dist/table'
import Markdown from 'braft-extensions/dist/markdown'
import CodeHighlighter from 'braft-extensions/dist/code-highlighter'
import 'braft-editor/dist/index.css'
import 'braft-extensions/dist/table.css'
import 'braft-extensions/dist/code-highlighter.css'
import 'prismjs/components/prism-java'
import 'prismjs/components/prism-php'

const options = {
  syntaxs: [
    { name: 'JavaScript', syntax: 'javascript' },
    { name: 'HTML', syntax: 'html' },
    { name: 'CSS', syntax: 'css' },
    { name: 'Java', syntax: 'java', },
    { name: 'PHP', syntax: 'php' }
  ]
}

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
    options: this.props.options
  }

  async componentDidMount() {
    console.log(this.state.options)
    const type = this.state.options.type;
    if (type === 'article') {
      notification['success']({
        key: 'tips',
        message: '快捷键支持！',
        description: '您可以使用Microsoft Word的常用快捷键，例如：按下 Control+S 或 Cmd+S 保存您当前创作的内容。',
        duration: 5,
      });
    } else if (type === 'question') {
      notification['info']({
        key: 'tips',
        message: '正在撰写答案',
        description: '您正在为问题 "' + this.state.options.body.title + '" 撰写答案，按下 Control+S 或 Cmd+S 保存，或点击右上角发布按钮发布答案。',
        duration: 8,
      });
    }
  }

  async componentWillUnmount() {
  }

  handleEditorChange = (editorState) => {
    this.setState({ editorState })
  }

  submitContent = async () => {
    const htmlContent = this.state.editorState.toHTML();
    console.log(htmlContent);
  }

  submitToServer = () => {
  }

  render() {
    const extendControls = [
      {
        key: 'submit',
        type: 'button',
        text: '发布',
        onClick: this.submitToServer
      }]
    const { editorState } = this.state
    return (
      <Typography>
        <BraftEditor
          onChange={this.handleEditorChange}
          onSave={this.submitContent}
          extendControls={extendControls}
          value={editorState} />
      </Typography>
    )
  }
}

export default Editor;