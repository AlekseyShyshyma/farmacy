import React, { Component } from 'react';
import {
    Upload, message, Button, Icon, Row, Col, Select, Form
} from 'antd';
import {addRow, getAllDrugstores, getAllMedicines, downloadFileByUrl} from '../apiUtils';
import '../styles/DataTable.css';

import '../styles/UploadPage.css';
import NonEditableTable from "../components/NonEditableTable";

const { Option } = Select;
const FormItem = Form.Item;

class DownloadPage extends Component {

    constructor(props) {
        super(props);
        this.state = {
            drugstores: [],
            uploadedFile: null,
            isUploading: false,
            hasUploaded: false,
        }
    }

    async componentDidMount() {
        const drugstores = await getAllDrugstores();

        this.setState({
            drugstores
        });
    }

    render() {

        const textColumns = [
            {
                title: 'Period Start',
                dataIndex: 'periodStart',
                width: '15%',
                editable: true,
            },
            {
                title: 'Period End',
                dataIndex: 'periodEnd',
                width: '15%',
                editable: true,
            },
        ];

        const numberColumns = [
            {
                title: 'Sold Id',
                dataIndex: 'soldId',
                width: '15%',
                editable: true,
            }, {
                title: 'Sum',
                dataIndex: 'sum',
                width: '15%',
                editable: true,
            }, {
                title: 'Amount',
                dataIndex: 'amount',
                width: '15%',
                editable: true,
            },
        ];

        const props = {
            name: 'file',
            action: '/api/sold/upload',
            headers: {
                authorization: 'authorization-text',
            },
            onChange: this.getUploadedSolds
        };

        const medicineProps = {
            name: 'file',
            action: '/api/medicine/upload',
            headers: {
                authorization: 'authorization-text',
            }
        };

        const drugstoreProps = {
            name: 'file',
            action: '/api/drugstore/upload',
            headers: {
                authorization: 'authorization-text',
            }
        };

        const { drugstores } = this.state;

        function downloadMedicines() {
            downloadFileByUrl("/api/medicine/export");
        }

        function downloadDrugstores() {
            downloadFileByUrl("/api/drugstore/export")
        }

        function downloadSoldIns() {
            downloadFileByUrl("/api/sold/export")
        }

        return(
            <div>
                <Row>
                    <Col span={8}>
                        <div className="upload" style={{height: '100%'}}>
                                <Button className="downloadButton" style={{height: '5em'}} onClick={downloadMedicines}>
                                    <Icon type="download" /> Download Medicines
                                </Button>
                        </div>
                    </Col>

                    <Col span={8}>
                        <div className="upload">
                                <Button className="downloadButton" style={{height: '5em'}} onClick={downloadDrugstores}>
                                    <Icon type="download" /> Download Drugstores
                                </Button>
                        </div>
                    </Col>

                    <Col span={2}>
                        <div className="upload">
                                <Button className="downloadButton" style={{height: '5em'}} onClick={downloadSoldIns}>
                                    <Icon type="download" /> Download Sold Ins
                                </Button>
                        </div>
                    </Col>
                    <Col span={4}>
                        <div className="upload" style={{marginLeft: 0, marginRight: 0, width: '100%'}}>
                            <FormItem style={{width: '100%'}}>
                                <Select
                                    style={{width: '100%'}}
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
                            </FormItem>
                        </div>
                    </Col>
                </Row>
            </div>
        );
    }

}

export default DownloadPage;