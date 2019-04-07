import React from 'react'
import BraftEditor from 'braft-editor'
import { notification } from "antd";
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
    {
      name: 'JavaScript',
      syntax: 'javascript'
    }, {
      name: 'HTML',
      syntax: 'html'
    }, {
      name: 'CSS',
      syntax: 'css'
    }, {
      name: 'Java',
      syntax: 'java',
    }, {
      name: 'PHP',
      syntax: 'php'
    }
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
    editorState: BraftEditor.createEditorState(null)
  }

  async componentDidMount() {
    notification['success']({
      message: '快捷键支持！',
      description: '您可以使用Microsoft Word的常用快捷键，例如：按下 Control+S 或 Cmd+S 保存您当前创作的内容。',
      duration: 8,
    });
  }

  handleEditorChange = (editorState) => {
    this.setState({ editorState })
  }

  submitContent = async () => {
    const htmlContent = this.state.editorState.toHTML();
    console.log(htmlContent);
  }

  submitToServer = () => {
    console.log("123")
  }

  render() {
    const extendControls = [
      {
        key: 'submit',
        type: 'button',
        text: '发布文章',
        onClick: this.submitToServer
      }]
    const { editorState } = this.state
    return (
      <BraftEditor
        onChange={this.handleEditorChange}
        onSave={this.submitContent}
        extendControls={extendControls}
        value={editorState} />
    )
  }
}

export default Editor;