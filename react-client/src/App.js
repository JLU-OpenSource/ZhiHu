import React from 'react';
import { LocaleProvider, Layout, Menu, Icon, Empty, Drawer } from 'antd';
import zhCN from 'antd/lib/locale-provider/zh_CN';
import './App.css';

const { Content, Footer, Sider } = Layout;
const SubMenu = Menu.SubMenu;

class App extends React.Component {
  state = {
    currentTab: 'recommend',
    showDrawer: false,
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

  onDrawerClose = (e) => {
    this.setState({
      showDrawer: false
    })
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
                <Empty style={{ marginTop: '100px' }} />
              </Content>
            </Layout>
          </Content>
          <Drawer
            visible={this.state.showDrawer}
            title="请登录"
            placement="right"
            onClose={this.onDrawerClose}
            closable={true}>
            <p>Some contents...</p>
            <p>Some contents...</p>
            <p>Some contents...</p>
          </Drawer>
          <Footer style={{ textAlign: 'center' }}>©2019 Powered by <a href='https://ant.design/'
            target='_blank' rel="noopener noreferrer">Ant Design</a></Footer>
        </Layout >
      </LocaleProvider>
    );
  }
}

export default App;
