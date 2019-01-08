import { API_BASE_URL, ACCESS_TOKEN } from '../constants';

const request = (options) => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    });

    if(localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
    }

    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options)
        .then(response =>
            response.json().then(json => {
                if(!response.ok) {
                    return Promise.reject(json);
                }
                return json;
            })
        );
};

export function login(loginRequest) {
    return request({
        url: 'api/manager/signin',
        method: 'POST',
        body: JSON.stringify(loginRequest)
    });
}

export function signup(signupRequest) {
    return request({
        url: 'api/manager/signup',
        method: 'POST',
        body: JSON.stringify(signupRequest)
    });
}

export function checkUsernameAvailability(username) {
    console.log(username);
    return request({
        url: "api/manager/checkManagerCodeAvailability?managerCode=" + username,
        method: 'GET'
    });
}


export function getCurrentUser() {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: 'api/manager/me',
        method: 'GET'
    });
}

export function getUserProfile(username) {
    return request({
        url: "/api/manager/get?managerCode=" + username,
        method: 'GET'
    });
}

export function getManagerDrugstores(username) {
    console.log('In server: ' + username);
    return request({
        url: "/api/drugstore/manager?managerCode=" + username,
        method: 'GET'
    });
}

export function getManagerMedicine(username) {
    console.log('In medicine server: ' + username);
    return request({
        url: "/api/medicine/manager?managerCode=" + username,
        method: 'GET'
    });
}

export function getManagerSolds(username) {
    console.log('In solds server: ' + username);
    return request({
        url: "/api/sold/manager?managerCode=" + username,
        method: 'GET'
    });
}

export function getAllManufacturers(username) {
    console.log('In manufacturers server: ' + username);
    return request({
        url: "/api/manufacturer/all",
        method: 'GET'
    });
}

export function updateRow(payload) {
    console.log(payload);
    if(payload.drugstoreCode) {
        updateDrugstore(payload);
    } else if(payload.medicineCode) {
        updateMedicine(payload)
    } else if(payload.soldId) {
        updateSoldInPeriod(payload)
    } else if(payload.code) {
        updateManufacturer(payload)
    }
}

function updateDrugstore(payload) {
    console.log('In update drugstore server: ' + payload);
    return request({
        url: "/api/drugstore/update?drugstoreCode=" + payload.drugstoreCode,
        method: 'PUT',
        body: JSON.stringify(payload),
    });
}

function updateManufacturer(payload) {
    console.log('In update manufacturer server: ' + payload);
    return request({
        url: "/api/manufacturer/update?manufacturerCode=" + payload.code,
        method: 'PUT',
        body: JSON.stringify(payload),
    });
}

function updateMedicine(payload) {
    console.log('In update medicine server: ' + payload);
    return request({
        url: "/api/medicine/update?medicineCode=" + payload.medicineCode,
        method: 'PUT',
        body: JSON.stringify(payload),
    });
}

function updateSoldInPeriod(payload) {
    console.log('In update medicine server: ' + payload);
    return request({
        url: "/api/sold/update?soldInPeriodCode=" + payload.soldId,
        method: 'PUT',
        body: JSON.stringify(payload),
    });
}

export function addRow(payload) {
    console.log(payload);
    if(payload.soldId) {
        addSoldInPeriod(payload);
    } else if(payload.drugstoreCode) {
        addDrugstore(payload)
    } else if(payload.medicineCode) {
        addMedicine(payload)
    } else if(payload.code) {
        addManufacturer(payload)
    }
}

function addDrugstore(payload) {
    console.log('In adding drugstore: ');
    return request({
       url: '/api/drugstore/new?managerCode=' + payload.managerCode,
       method: 'POST',
        body: JSON.stringify(payload)
    });
}

function addMedicine(payload) {
    console.log('In adding medicine: ');
    return request({
        url: '/api/medicine/new?manufacturerCode=' + payload.manufacturerCode,
        method: 'POST',
        body: JSON.stringify(payload)
    });
}

function addManufacturer(payload) {
    console.log('In adding manufacturer: ');
    return request({
        url: '/api/manufacturer/new',
        method: 'POST',
        body: JSON.stringify(payload)
    });
}

function addSoldInPeriod(payload) {
    console.log('In adding sold: ');
    return request({
        url: '/api/sold/new?drugstoreCode=' + payload.drugstoreCode + '&medicineCode=' + payload.medicineCode,
        method: 'POST',
        body: JSON.stringify(payload)
    });
}
