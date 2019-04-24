import React from 'react';
import { LocaleProvider, Layout, Menu, Icon, Drawer, Empty, Card, Row, Col } from 'antd';
import zhCN from 'antd/lib/locale-provider/zh_CN';
import Editor from './componet/Editor.js'
import LoginForm from './componet/form/LoginForm.js';
import Recommends from './componet/Recommeds.js'
import Questions from './componet/Questions.js';
import Answer from "./componet/Answer.js";
import Article from './componet/Article.js';
import Answers from './componet/Answers.js';
import Collects from './componet/Collects.js';

import './App.css';
import UserApi from './api/UserApi.js';
import Drafts from './componet/Drafts.js';
import UserPanel from './componet/UserPanel.js';


const { Content, Footer, Sider } = Layout;
const SubMenu = Menu.SubMenu;
const { Meta } = Card;

class App extends React.Component {

  state = {
    tab: 'recommend',
    content: 'recommend',
    loginFormVisible: true,
    editorOptions: {
      type: 'question', body: {}
    },
    currentUser: null,
    answer: null,
    article: null,
    question: null
  }

  async componentDidMount() {
    const _this = this;
    UserApi.getCurrentUser(function (user) {
      _this.setState({ currentUser: user })
    });
  }

  handleTabClick = (e) => {
    if (e.key === 'user-panel')
      this.setState({ loginFormVisible: true, });
    else {
      switch (e.key) {
        case 'editor':
          if (this.state.currentUser == null) {
            this.setState({ loginFormVisible: true, });
          } else
            this.setState({
              content: 'editor',
              tab: 'editor',
              editorOptions: {
                type: 'question', body: {}
              }
            });
          break;
        case 'drafts':
          if (this.state.currentUser == null) {
            this.setState({ loginFormVisible: true, });
          } else this.setState({
            content: e.key,
            tab: e.key
          });
          break;
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
        type: 'answer', body: question
      }
    })
  }

  handleAllAnswerClick = (body) => {
    console.log(body)
    this.setState({
      content: 'answers',
      question: body
    })
  }

  handleFullAnswerClick = (answer) => {
    this.setState({
      content: 'answer',
      answer: answer
    })
  }

  handleDraftRestore = (draft) => {
    this.setState({
      content: 'editor',
      tab: 'editor',
      editorOptions: {
        type: 'draft', body: draft
      }
    });
  }

  getContent = () => {
    switch (this.state.content) {
      case 'recommend': return <Recommends fullAnswerClick={this.handleFullAnswerClick} />;
      case 'answer': return <Answer createAnswerClick={this.handleCreateAnswerClick} answer={this.state.answer} />;
      case 'answers': return <Answers qid={this.state.question.key} fullAnswerClick={this.handleFullAnswerClick} />
      case 'question': return <Questions
        createAnswerClick={this.handleCreateAnswerClick}
        allAnswerClick={this.handleAllAnswerClick} />;
      case 'article': return <Article article={this.state.article} />
      case 'editor': return <Editor options={this.state.editorOptions}
        onSubmitQuestion={() => this.setState({ content: 'question', tab: 'question' })}
        onSubmitArticle={(article) => this.setState({ content: 'article', article: article, tab: 'article' })} />;
      case 'collection': return <Collects />;
      case 'drafts': return <Drafts restoreDraft={this.handleDraftRestore} />
      case 'android': return <div style={{ textAlign: 'center', marginTop: '100px' }}>
        <Row>
          <Col span={8} offset={8}>
            <Card
              hoverable
              style={{ width: 240 }}
              cover={<img alt="example" src="http://47.94.134.55:8080/code.png" />}
            >
              <Meta
                title="下载知乎Android Apk"
                description="发现更大的世界"
              />
            </Card>
          </Col>
        </Row>
      </div>
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
                  <Menu.Item key="user-panel"><Icon type="user" />{this.state.currentUser == null ? '未登陆' : this.state.currentUser.name}</Menu.Item>
                  <SubMenu key="home" title={<span><Icon type="home" />首页</span>}>
                    <Menu.Item key="recommend"><span><Icon type="fire" />推荐</span></Menu.Item>
                    <Menu.Item key="question"><span><Icon type="question-circle" />问题</span></Menu.Item>
                    <Menu.Item key="article"><span><Icon type="read" />文章</span></Menu.Item>
                  </SubMenu>
                  <Menu.Item key="editor"><Icon type="edit" />编辑器</Menu.Item>
                  <Menu.Item key="collection"><span><Icon type="tags" />收藏夹</span></Menu.Item>
                  <Menu.Item key="drafts"><span><Icon type="file" />草稿箱</span></Menu.Item>
                  <Menu.Item key="about"><span><Icon type="info-circle" />关于本站</span></Menu.Item>
                  <Menu.Item key="android"><span><Icon type="android" />下载Apk</span></Menu.Item>
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
            title={this.state.currentUser == null ? '请登录' : this.state.currentUser.name}
            placement="right"
            onClose={this.onDrawerClose}
            closable={true}>
            {this.state.currentUser == null ? <LoginForm /> : <UserPanel user={JSON.parse(sessionStorage.getItem('user'))} />}
          </Drawer>
          <Footer style={{ textAlign: 'center' }}>©2019 Powered by <a href='https://ant.design/'
            target='_blank' rel="noopener noreferrer">Ant Design</a></Footer>
        </Layout >
      </LocaleProvider>
    );
  }
}

export default App;
