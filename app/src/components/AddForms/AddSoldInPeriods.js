import React, { Component } from 'react';
import {Form, Input, Button, Icon, notification, Row, Col, Select} from 'antd';
import '../../styles/LoginPage.css';
import {addRow, getManagerDrugstores, getAllMedicines} from '../../apiUtils';

const { Option } = Select;
const FormItem = Form.Item;

class AddSoldInPeriod extends Component {
    constructor(props) {
        super(props);
    };

    render() {
        const AntWrappedLoginForm = Form.create()(AddSoldInPeriodForm);
        return (
            <div className="login-container">
                <div className="login-content">
                    <AntWrappedLoginForm
                        username = {this.props.username}
                        onAdding={this.props.onAdding}/>
                </div>
            </div>
        );
    }
}

class AddSoldInPeriodForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            drugstores: [],
            medicines: []
        };
        this.handleSubmit = this.handleSubmit.bind(this);
    };

    async componentDidMount() {

        console.log(this.props);

        const drugstoresResp = await getManagerDrugstores(this.props.username);
        const drugstores = drugstoresResp.content;
        const medicines = await getAllMedicines();

        console.log(drugstores);
        console.log(medicines);

        this.setState({
            drugstores,
            medicines
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

        const {drugstores, medicines} = this.state;

        return(
            <div>
                <Form onSubmit={this.handleSubmit} className="login-form">
                    <Row>
                        <Col span={12}>
                            <FormItem>
                                {getFieldDecorator('soldId', {
                                    rules: [{ required: true, message: 'Please input sold id!' }],
                                })(
                                    <Input
                                        prefix={<Icon type="user" />}
                                        size="large"
                                        name="soldId"
                                        placeholder="Sold Id" />
                                )}
                            </FormItem>
                        </Col>
                        <Col span={12}>
                            <FormItem>
                                {getFieldDecorator('sum', {
                                    rules: [{ required: true, message: 'Please input sum!' }],
                                })(
                                    <Input
                                        prefix={<Icon type="lock" />}
                                        size="large"
                                        name="sum"
                                        type="number"
                                        placeholder="Sum"  />
                                )}
                            </FormItem>
                        </Col>
                    </Row>
                    <Row>
                        <Col span={12}>
                            <FormItem>
                                {getFieldDecorator('drugstoreCode', {
                                    rules: [{ required: true, message: 'Please input drugstore code!' }],
                                })(
                                    <Select
                                        showSearch
                                        placeholder="Select a drugstore"
                                        optionFilterProp="children"
                                        filterOption={(input, option) =>
                                            option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                                        }
                                    >
                                        {drugstores.map((drugstore) =>
                                            <Option key={drugstore.drugstoreCode} value={drugstore.drugstoreCode}>{drugstore.networkTitle}</Option>
                                        )}
                                    </Select>
                                )}
                            </FormItem>
                        </Col>
                        <Col span={12}>
                            <FormItem>
                                {getFieldDecorator('medicineCode', {
                                    rules: [{ required: true, message: 'Please input medicine code!' }],
                                })(
                                    <Select
                                        showSearch
                                        placeholder="Select a medicine"
                                        optionFilterProp="children"
                                        filterOption={(input, option) =>
                                            option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                                        }
                                    >
                                        {medicines.map((medicine) =>
                                            <Option key={medicine.title} value={medicine.medicineCode}>{medicine.title}</Option>
                                        )}
                                    </Select>
                                )}
                            </FormItem>
                        </Col>
                    </Row>
                    <Row>
                        <Col span={8}>
                            <FormItem>
                                {getFieldDecorator('amount', {
                                    rules: [{ required: true, message: 'Please input amount!' }],
                                })(
                                    <Input
                                        prefix={<Icon type="lock" />}
                                        size="large"
                                        name="amount"
                                        type="number"
                                        placeholder="Amount"  />
                                )}
                            </FormItem>
                        </Col>
                        <Col span={8}>
                            <FormItem>
                                {getFieldDecorator('periodStart', {
                                    rules: [{ required: true, message: 'Please input period start!' }],
                                })(
                                    <Input
                                        prefix={<Icon type="lock" />}
                                        size="large"
                                        name="periodStart"
                                        type="text"
                                        placeholder="Period Start"  />
                                )}
                            </FormItem>
                        </Col>
                        <Col span={8}>
                            <FormItem>
                                {getFieldDecorator('periodEnd', {
                                    rules: [{ required: true, message: 'Please input period end!' }],
                                })(
                                    <Input
                                        prefix={<Icon type="lock" />}
                                        size="large"
                                        name="periodEnd"
                                        type="text"
                                        placeholder="Period End"  />
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

export default AddSoldInPeriod;