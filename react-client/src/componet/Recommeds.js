import React from 'react';
import { List, Avatar, Icon, Empty, Pagination } from 'antd';

const listData = [];
for (let i = 0; i < 3; i++) {
  listData.push({
    href: '#',
    title: `知乎刘看山`,
    avatar: 'https://s2.ax1x.com/2019/04/02/A6ab3d.jpg',
    description: '发现更大的世界',
    content: 'Markdown 是一个 Web 上使用的文本到HTML的转换工具，可以通过简单、易读易写的文本格式生成结构化的HTML文档。目前 github、Stackoverflow 等网站均支持这种格式。',
  });
}

const IconText = ({ type, text }) => (
  <span>
    <Icon type={type} style={{ marginRight: 8 }} />
    {text}
  </span>
);

class Recommends extends React.Component {
  render() {
    return (
      <List
        itemLayout="vertical"
        size="large"
        locale={<Empty />}
        dataSource={listData}
        footer={<Pagination simple pageSize={3} defaultCurrent={1} total={8} style={{ marginTop: '10px', textAlign: 'center' }} />}
        renderItem={item => (
          <List.Item
            key={item.title}
            actions={
              [<IconText type="star-o" text="156 收藏" />, <IconText type="like-o" text="156 评论" />,
              <IconText type="message" text="2 评论" />, <IconText type="read" text="查看原答案" />]}>
            <a href='javascrpit:void(0)'><h3>什么是MarkDown？如何使用MarkDown？</h3></a>
            <List.Item.Meta
              avatar={<Avatar src={item.avatar} />}
              title={<a href={item.href}>{item.title}</a>}
              description={item.description}
            />
            {item.content}
          </List.Item>
        )}
      />
    );
  }
}

export default Recommends;