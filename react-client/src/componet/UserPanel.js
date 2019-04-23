import React from 'react';
import '../App.css';

import { List, Avatar, Tabs, Divider } from 'antd';

const TabPane = Tabs.TabPane;

class UserPanel extends React.Component {

  state = {
    user: this.props.user
  }

  render() {
    return (
      <div>
        <List.Item.Meta
          avatar={<Avatar src={this.state.user.avatar} />}
          title={this.state.user.name}
          description={this.state.user.sign}
        />
        <Divider />
        <Tabs defaultActiveKey="1">
          <TabPane tab="回答" key="1">后续开放</TabPane>
          <TabPane tab="提问" key="2">后续开放</TabPane>
          <TabPane tab="文章" key="3">后续开放</TabPane>
          <TabPane tab="收藏" key="4">后续开放</TabPane>
        </Tabs>
      </div>

    );
  }
}

export default UserPanel;