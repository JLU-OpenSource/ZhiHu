import React from 'react';
import { Layout, Menu, Icon } from 'antd';
import './App.css';

const { Content, Footer, Sider } = Layout;
const SubMenu = Menu.SubMenu;

class App extends React.Component {
  render() {
    return (
      <Layout>
        <Content style={{ padding: '0 100px', margin: '50px 0 0px' }}>
          <Layout style={{ padding: '24px 0', background: '#fff' }}>
            <Sider width={200} style={{ background: '#fff' }}>
              <Menu
                mode="inline"
                defaultSelectedKeys={['recommend']}
                defaultOpenKeys={['home']}
                style={{ height: '100%' }}
              >
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
              <Menu mode="horizontal">
                <Menu.Item key="create-article"><Icon type="edit" />撰写文章</Menu.Item>
                <Menu.Item key="submit-question"><Icon type="question" />发布问题</Menu.Item>
                <Menu.Item key="user-panel" style={{ float: 'right' }}><Icon type="user" />未登陆</Menu.Item>
              </Menu>
            </Content>
          </Layout>
        </Content>
        <Footer style={{ textAlign: 'center' }}>©2019 Powered by <a href='https://ant.design/'
          target='_blank' rel="noopener noreferrer">Ant Design</a></Footer>
      </Layout >
    );
  }
}

export default App;
