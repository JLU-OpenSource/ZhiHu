import React from 'react';
import { Comment, Tooltip, List, Icon, } from 'antd';
import moment from 'moment';
import 'moment/locale/zh-cn';

moment.locale('zh-cn');

const actions = [
  <span>
    <Tooltip title="Like">
      <Icon type="like" />
    </Tooltip>
    <span style={{ paddingLeft: 8, cursor: 'auto' }}>
      0
    </span>
  </span>,
  <span>
    <Tooltip title="Dislike">
      <Icon type="dislike" />
    </Tooltip>
    <span style={{ paddingLeft: 8, cursor: 'auto' }}>
      0
    </span>
  </span>,
  <span>Reply to</span>,
];

const data = [
  {
    actions: actions,
    author: 'Han Solo',
    avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
    content: (
      <p>We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.</p>
    ),
    datetime: (
      <Tooltip title={moment().subtract(1, 'days').format('YYYY-MM-DD HH:mm:ss')}>
        <span>{moment().subtract(1, 'days').fromNow()}</span>
      </Tooltip>
    ),
  },
  {
    author: 'Han Solo',
    avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
    content: (
      <p>We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.</p>
    ),
    datetime: (
      <Tooltip title={moment().subtract(2, 'days').format('YYYY-MM-DD HH:mm:ss')}>
        <span>{moment().subtract(2, 'days').fromNow()}</span>
      </Tooltip>
    ),
  },
];

class Comments extends React.Component {

  render() {
    return (
      <List
        className="comment-list"
        itemLayout="horizontal"
        dataSource={data}
        renderItem={item => (
          <Comment
            actions={item.actions}
            author={item.author}
            avatar={item.avatar}
            content={item.content}
            datetime={item.datetime}
          />
        )}
      />);
  }
}

export default Comments;