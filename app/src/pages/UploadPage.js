import React, { Component } from 'react';
import {
    Upload, message, Button, Icon,
} from 'antd';
import '../styles/DataTable.css';

import '../styles/UploadPage.css';
import NonEditableTable from "../components/NonEditableTable";

class UploadPage extends Component {

    constructor(props) {
        super(props);
        this.state = {
            uploadedFile: null,
            isUploading: false,
            uploadedSolds: null,
            hasUploaded: false,
        }
    }

    getUploadedSolds = (info) => {
        if (info.file.status !== 'uploading') {
            console.log(info.file, info.fileList);
        }
        if (info.file.status === 'done') {
            message.success(`${info.file.name} file uploaded successfully`);
            const uploadedSolds = info.fileList[0].response;
            this.setState({uploadedSolds: uploadedSolds});
            this.setState({hasUploaded: true});
        } else if (info.file.status === 'error') {
            message.error(`${info.file.name} file upload failed.`);
        }
    };

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

        return(
            <div>
                <div className="upload">
                    <Upload {...props}>
                        <Button className="uploadButton">
                            <Icon type="upload" /> Click to Upload
                        </Button>
                    </Upload>
                </div>
                <div>
                    <NonEditableTable className="table" numberColumns={numberColumns} textColumns={textColumns} loadedDrugstores={this.state.uploadedSolds} isSold={true}/>
                </div>
            </div>
        );
    }

}

export default UploadPage;