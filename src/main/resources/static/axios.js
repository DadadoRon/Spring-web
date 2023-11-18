var ax = {
    async get(url) {
        const authToken = localStorage.getItem("authToken")
        return await axios.get(url , {
            headers: {
                Authorization: `${authToken}`,
                'Content-Type': 'application/json'
            }
        })
    }
};
