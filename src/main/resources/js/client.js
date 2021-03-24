client = (function (){

    const validateInputs = (username, password) => {
        if (username === "" || (username.includes('<') && username.includes('>')) ||
            password === "" || (password.includes('<') && password.includes('>'))) {
            swal({
                icon: 'error',
                title: 'Oops...',
                text: 'Something went wrong. Check your inputs and try again.',
            });
            return false;
        }
        return true;
    };

    const login = (username, password) => {
        if  (validateInputs(username, password)){
            let user = {
                "username": username,
                "password": password
            }

            fetch('/login', {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(user)
            }).then( res => {
                if (res.ok){
                    window.location.href = "/private/otherService";
                }else {
                    swal({
                        icon: 'error',
                        title: 'Oops...',
                        text: 'Something went wrong. Check your inputs and try again.',
                    });
                }
            }).catch( () => alert("not logged"));
        }
    };

    return{
        login:login
    }

})();