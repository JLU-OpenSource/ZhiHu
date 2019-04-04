import React from 'react';
import {
    Form, Icon, Input, Button, Collapse
} from 'antd';

const Panel = Collapse.Panel;
const showMore = "显示更多内容";
const showLess = "显示较少内容";
class LoginForm extends React.Component {

    state = {
        collapseInfo: showMore
    }

    handleSubmit = (e) => {
        e.preventDefault();
    }

    handleCollapseChange = (e) => {
        this.setState({
            collapseInfo: e.length > 0 ? showLess : showMore
        })

    }

    render() {
        return (
            <Form onSubmit={this.handleSubmit}>
                <Form.Item>
                    <Input allowClear required size='large' prefix={<Icon type="mail" style={{ color: 'rgba(0,0,0,.25)' }} />} type="email" placeholder="邮箱地址" />
                </Form.Item>
                <Form.Item style={{ marginBottom: '0px' }}>
                    <Input.Password allowClear required size='large' prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />} type="password" placeholder="输入密码" />
                </Form.Item>
                <Form.Item style={{ marginBottom: '0px' }}>
                    <Collapse bordered={false} onChange={this.handleCollapseChange}>
                        <Panel header={this.state.collapseInfo} showArrow={false}>
                            <Form.Item>
                                <Input allowClear size='large' prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)', padding: '0px' }} />} type="text" placeholder="用户昵称" />
                            </Form.Item>
                            <Form.Item>
                                <Input allowClear size='large' prefix={<Icon type="smile" style={{ color: 'rgba(0,0,0,.25)', padding: '0px' }} />} type="url" placeholder="头像链接" />
                            </Form.Item>
                            <Form.Item>
                                <Input allowClear size='large' prefix={<Icon type="cloud" style={{ color: 'rgba(0,0,0,.25)', padding: '0px' }} />} type="url" placeholder="个人站点" />
                            </Form.Item>
                        </Panel>
                    </Collapse>
                </Form.Item>
                <Form.Item>
                    <Button size='large' type="primary" htmlType="submit" block>登陆</Button>
                </Form.Item>
                <div style={{ textAlign: 'center' }}><p>未注册用户自动注册为本站用户</p></div>

            </Form>

        );
    }
}

export default LoginForm