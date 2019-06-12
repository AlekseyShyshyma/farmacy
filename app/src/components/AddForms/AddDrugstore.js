import React, { Component } from 'react';
import { Form, Input, Button, Icon, notification, Row, Col, Select } from 'antd';
import '../../styles/LoginPage.css';
import {addRow, getAllManagers} from '../../apiUtils';

const { Option } = Select;
const FormItem = Form.Item;

class AddDrugstore extends Component {
    constructor(props) {
        super(props);
    };

    render() {
        const AntWrappedLoginForm = Form.create()(AddDrugstoreForm);
        return (
            <div className="login-container">
                <div className="login-content">
                    <AntWrappedLoginForm onAdding={this.props.onAdding}/>
                </div>
            </div>
        );
    }
}

class AddDrugstoreForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            managers: []
        };
        this.handleSubmit = this.handleSubmit.bind(this);
    };

    async componentDidMount() {
        const managers = await getAllManagers();
        console.log(managers);

        this.setState({
            managers
        });
    }

    handleSubmit(event) {
        event.preventDefault();
        this.props.form.validateFields((err, values) => {
            if (!err) {
                const newMedicine = Object.assign({}, values);
                console.log(newMedicine);
                addRow(newMedicine).then((res) => {
                    this.props.onAdding();
                }).catch(error => {
                    notification.error({
                        message: 'Pharmacy App',
                        description: error.message || 'Sorry! Something went wrong. Please try again!'
                    });
                });
            }
        });

    }

    render() {
        const { getFieldDecorator } = this.props.form;
        const { managers } = this.state;
        return(
            <div>
                <Form onSubmit={this.handleSubmit} className="login-form">
                    <Row>
                        <Col span={12}>
                            <FormItem>
                                {getFieldDecorator('drugstoreCode', {
                                    rules: [{ required: true, message: 'Please input drugstore code!' }],
                                })(
                                    <Input
                                        prefix={<Icon type="user" />}
                                        size="large"
                                        name="drugstoreCode"
                                        placeholder="Drugstore Code" />
                                )}
                            </FormItem>
                        </Col>
                        <Col span={12}>
                            <FormItem>
                                {getFieldDecorator('address', {
                                    rules: [{ required: true, message: 'Please input title!' }],
                                })(
                                    <Input
                                        prefix={<Icon type="lock" />}
                                        size="large"
                                        name="address"
                                        type="text"
                                        placeholder="Address"  />
                                )}
                            </FormItem>
                        </Col>
                    </Row>
                    <Row>
                        <Col span={12}>
                            <FormItem>
                                {getFieldDecorator('networkTitle', {
                                    rules: [{ required: true, message: 'Please input title!' }],
                                })(
                                    <Input
                                        prefix={<Icon type="lock" />}
                                        size="large"
                                        name="networkTitle"
                                        type="text"
                                        placeholder="Title"  />
                                )}
                            </FormItem>
                        </Col>
                        <Col span={12}>
                            <FormItem>
                                {getFieldDecorator('phoneNumber', {
                                    rules: [{ required: true, message: 'Please input expiration term!' }],
                                })(
                                    <Input
                                        prefix={<Icon type="lock" />}
                                        size="large"
                                        name="phoneNumber"
                                        type="number"
                                        placeholder="Phone"  />
                                )}
                            </FormItem>
                        </Col>
                    </Row>
                    <Row>
                        <Col span={12}>
                            <FormItem>
                                {getFieldDecorator('region', {
                                    rules: [{ required: true, message: 'Please input measurement unit!' }],
                                })(
                                    <Input
                                        prefix={<Icon type="lock" />}
                                        size="large"
                                        name="region"
                                        type="text"
                                        placeholder="Region"  />
                                )}
                            </FormItem>
                        </Col>
                        <Col span={12}>
                            <FormItem>
                                {getFieldDecorator('managerCode', {
                                    rules: [{ required: true, message: 'Please input manager code!' }],
                                })(
                                    <Select
                                        showSearch
                                        placeholder="Select a manager"
                                        optionFilterProp="children"
                                        filterOption={(input, option) =>
                                            option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                                        }
                                    >
                                        {managers.map((manager) =>
                                            <Option key={manager.surname} value={manager.managerCode}>{manager.name + ' ' + manager.surname}</Option>
                                        )}
                                    </Select>
                                )}
                            </FormItem>
                        </Col>
                    </Row>
                    <FormItem>
                        <Button type="primary" htmlType="submit" size="large" className="login-form-button">Add</Button>
                    </FormItem>
                </Form>
            </div>
        );
    }
}

export default AddDrugstore;