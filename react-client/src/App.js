import React from 'react';
import { LocaleProvider, Layout, Menu, Icon, Drawer } from 'antd';
import zhCN from 'antd/lib/locale-provider/zh_CN';
import SimpleMDE from "react-simplemde-editor";
import LoginForm from './componet/form/LoginForm.js';
import Recommends from './componet/Recommeds.js'
import Comments from './componet/Comments.js'

import "easymde/dist/easymde.min.css";
import './App.css';

const { Content, Footer, Sider } = Layout;
const SubMenu = Menu.SubMenu;
const MdeToolbar = [
  "bold", "italic", "heading", "|", "quote", "unordered-list",
  "ordered-list", "link", "image", "table", "|", "side-by-side",
  "fullscreen", "preview", "|",
  {
    name: "发布",
    action: function customFunction(editor) {
      alert("submit")
    },
    className: "fa fa-check",
    title: "发布",
  },
  {
    name: "custom",
    action: function customFunction(editor) {
      alert("save")
    },
    className: "fa fa-file",
    title: "保存",
  }
];

class App extends React.Component {
  state = {
    currentTab: 'recommend',
    showDrawer: false,
    mdeCotent: ''
  }

  handleClick = (e) => {
    if (e.key === 'user-panel')
      this.setState({
        showDrawer: true
      });
    else
      this.setState({
        currentTab: e.key,
      }, () => {
        console.log(this.state.currentTab);
      });
  }

  handleMdeChange = value => {
    this.setState({
      mdeCotent: value
    });
  }

  onDrawerClose = (e) => {
    this.setState({
      showDrawer: false
    })
  }

  getContent = () => {
    switch (this.state.currentTab) {
      case 'question': return <Comments />;
      case 'create-article': return <SimpleMDE id='mde'
        onChange={this.handleMdeChange}
        value={this.state.mdeCotent}
        options={{
          autofocus: true,
          toolbar: MdeToolbar,
          spellChecker: false,
          status: false,
          renderingConfig: {
            singleLineBreaks: false,
            codeSyntaxHighlighting: true,
          },
        }} />
      default: return <Recommends />;
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
                  onClick={this.handleClick} selectedKeys={[this.state.currentTab]}
                  style={{ height: '100%' }}>
                  <SubMenu key="home" title={<span><Icon type="home" />首页</span>}>
                    <Menu.Item key="recommend"><span><Icon type="fire" />推荐</span></Menu.Item>
                    <Menu.Item key="question"><span><Icon type="question-circle" />问题</span></Menu.Item>
                    <Menu.Item key="article"><span><Icon type="read" />文章</span></Menu.Item>
                    <Menu.Item key="idea"><span><Icon type="star" />想法</span></Menu.Item>
                  </SubMenu>
                  <Menu.Item key="collection"><span><Icon type="tags" />收藏夹</span></Menu.Item>
                  <Menu.Item key="draft"><span><Icon type="file" />草稿箱</span></Menu.Item>
                  <Menu.Item key="about"><span><Icon type="info-circle" />关于本站</span></Menu.Item>
                  <Menu.Item key="copyright"><span><Icon type="copyright" />版权所有</span></Menu.Item>
                </Menu>
              </Sider>
              <Content style={{ padding: '0 24px', minHeight: 600 }}>
                <Menu onClick={this.handleClick} selectedKeys={[this.state.currentTab]} mode="horizontal">
                  <Menu.Item key="create-article"><Icon type="edit" />撰写文章</Menu.Item>
                  <Menu.Item key="submit-question"><Icon type="question" />发布问题</Menu.Item>
                  <Menu.Item key="user-panel" style={{ float: 'right' }}>
                    <Icon type="user" />未登陆</Menu.Item>
                </Menu>
                {this.getContent()}
              </Content>
            </Layout>
          </Content>
          <Drawer
            width='500'
            visible={this.state.showDrawer}
            title="请登录"
            placement="right"
            onClose={this.onDrawerClose}
            closable={true}>
            <LoginForm name={this.state.currentTab} />
          </Drawer>
          <Footer style={{ textAlign: 'center' }}>©2019 Powered by <a href='https://ant.design/'
            target='_blank' rel="noopener noreferrer">Ant Design</a></Footer>
        </Layout >
      </LocaleProvider>
    );
  }
}

export default App;
