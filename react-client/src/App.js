import React from 'react';
import { LocaleProvider, Layout, Menu, Icon, Drawer, Empty } from 'antd';
import zhCN from 'antd/lib/locale-provider/zh_CN';
import Editor from './componet/Editor.js'
import LoginForm from './componet/form/LoginForm.js';
import Recommends from './componet/Recommeds.js'
import Questions from './componet/Questions.js';
import Comments from './componet/Comments.js';
import Answer from "./componet/Answer.js";

import './App.css';
import Article from './componet/Article.js';

const { Content, Footer, Sider } = Layout;
const SubMenu = Menu.SubMenu;

class App extends React.Component {
  state = {
    tab: 'recommend',
    content: 'recommend',
    loginFormVisible: false,
    editorOptions: {
      type: 'article', body: {}
    }
  }

  handleTabClick = (e) => {
    if (e.key === 'user-panel')
      this.setState({ loginFormVisible: true, });
    else {
      switch (e.key) {
        case 'editor': this.setState({
          content: 'editor',
          tab: 'editor',
          editorOptions: {
            type: 'article', body: {}
          }
        }); break;
        default: this.setState({
          content: e.key,
          tab: e.key
        });
      }
    }
  }

  onDrawerClose = (e) => {
    this.setState({ loginFormVisible: false });
  }

  handleCreateAnswerClick = (question) => {
    this.setState({
      content: 'editor',
      tab: 'editor',
      editorOptions: {
        type: 'question', body: question
      }
    })
  }

  handleFullAnswerClick = (question) => {
    this.setState({
      content: 'answer',
      tab: 'recommend'
    })
  }

  getContent = () => {
    switch (this.state.content) {
      case 'recommend': return <Recommends fullAnswerClick={this.handleFullAnswerClick} />;
      case 'answer': return <Answer />;
      case 'question': return <Questions createAnswerClick={this.handleCreateAnswerClick} />;
      case 'article': return <Article />
      case 'editor': return <Editor options={this.state.editorOptions} />;
      case 'idea': return <Comments />
      default: return <Empty style={{ marginTop: '200px' }} />;
    }
  }

  render() {
    return (
      <LocaleProvider locale={zhCN}>
        <Layout>
          <Content style={{ padding: '0 100px', margin: '50px 0 0px' }}>
            <Layout style={{ padding: '24px 0', background: '#fff' }}>
              <Sider width={200} style={{ background: '#fff' }}>
                <Menu
                  mode="inline"
                  defaultSelectedKeys={['recommend']}
                  defaultOpenKeys={['home']}
                  onClick={this.handleTabClick} selectedKeys={[this.state.tab]}
                  style={{ height: '100%' }}>
                  <Menu.Item key="user-panel"><Icon type="user" />未登陆</Menu.Item>
                  <SubMenu key="home" title={<span><Icon type="home" />首页</span>}>
                    <Menu.Item key="recommend"><span><Icon type="fire" />推荐</span></Menu.Item>
                    <Menu.Item key="question"><span><Icon type="question-circle" />问题</span></Menu.Item>
                    <Menu.Item key="article"><span><Icon type="read" />文章</span></Menu.Item>
                    <Menu.Item key="idea"><span><Icon type="star" />想法</span></Menu.Item>
                  </SubMenu>
                  <Menu.Item key="editor"><Icon type="edit" />编辑器</Menu.Item>
                  <Menu.Item key="collection"><span><Icon type="tags" />收藏夹</span></Menu.Item>
                  <Menu.Item key="draft"><span><Icon type="file" />草稿箱</span></Menu.Item>
                  <Menu.Item key="about"><span><Icon type="info-circle" />关于本站</span></Menu.Item>
                  <Menu.Item key="copyright"><span><Icon type="copyright" />版权所有</span></Menu.Item>
                </Menu>
              </Sider>
              <Content style={{ padding: '0 24px', minHeight: 620 }}>
                {this.getContent()}
              </Content>
            </Layout>
          </Content>
          <Drawer
            width={500}
            visible={this.state.loginFormVisible}
            title='请登录'
            placement="right"
            onClose={this.onDrawerClose}
            closable={true}>
            <LoginForm />
          </Drawer>
          <Footer style={{ textAlign: 'center' }}>©2019 Powered by <a href='https://ant.design/'
            target='_blank' rel="noopener noreferrer">Ant Design</a></Footer>
        </Layout >
      </LocaleProvider>
    );
  }
}

export default App;
