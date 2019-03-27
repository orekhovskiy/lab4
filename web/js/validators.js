function passwordCheck(login, password) {
    if (login.length < 5 || login.length > 20)
        return "Длина логина должна быть в диапазоне от 5 до 20 символов!";
    if (password.length < 5 || password.length > 20)
        return "Длина пароля должна быть в диапазоне от 5 до 20 символов!";
}

function validateSignup(form) {
    var login = form.login.value;
    var password = form.password.value;
    var repeat_password = form.repeatPassword.value;
    var error = passwordCheck(login, password);

    if (password !== repeat_password)
        error = "Пароли не совпадают!";

    if (error){
        $("#errorLabel").text(error);
        return false;
    } 
    return true;
}

function validateLogin(form) {
    var login = form.login.value;
    var password = form.password.value;
    var error = passwordCheck(login, password);

    if (error){
        $("#errorLabel").text(error);
        return false;
    }
    return true;
}

function validateInput(form) {
    function isNumeric(n) {
        return !isNaN(parseFloat(n)) && isFinite(n);
    }

    var y = form.Y.value;
    var error;

    if (!isNumeric(y)) error = "Y введён некорректно.";
    else if (y < -5 || y > 5) error = "Y должен быть в диапазоне от -5 до 5.";

    var selected = false;
    var input = form.querySelector('[name="xGroup"]');
            for (var i = 0; i < input.length; i++){
                if (input[i].checked){
                    selected = true;
                    break;
                }
            }
    if (!selected) error = "Выберите X.";

    var selected = false;
    var input = form.querySelector('[name="rGroup"]');
            for (var i = 0; i < input.length; i++){
                if (input[i].checked){
                    selected = true;
                    break;
                }
            }
    if (!selected) error = "Выберите R.";
    if (error){
        $("#errorLabel").text(error);
        return false;
    }
    $("#errorLabel").text("");
    return true;
}