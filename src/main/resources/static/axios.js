var ax = {
    async get(url) {
        const authToken = localStorage.getItem("authToken")
        return await axios.get(url , {
            headers: {
                Authorization: `${authToken}`,
                'Content-Type': 'application/json'
            }
        })
    },
    async post(url, data) {
        const authToken = localStorage.getItem("authToken")
        return await axios.post(url , data, {
            headers: {
                Authorization: `${authToken}`,
                'Content-Type': 'application/json'
            }
        })
    },
    async put(url, data) {
        const authToken = localStorage.getItem("authToken")
        return await axios.put(url , data, {
            headers: {
                Authorization: `${authToken}`,
                'Content-Type': 'application/json'
            }
        })
    },
    async delete(url) {
        const authToken = localStorage.getItem("authToken")
        return await axios.delete(url , {
            headers: {
                Authorization: `${authToken}`,
                'Content-Type': 'application/json'
            }
        })
    }
};




