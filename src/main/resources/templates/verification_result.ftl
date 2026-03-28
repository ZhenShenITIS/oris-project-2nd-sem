<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Подтверждение аккаунта</title>
</head>
<body>
<h2>Статус активации аккаунта</h2>

<#if success>
    <p style="color: green;">Ваша почта успешно подтверждена! Теперь вы можете войти в свой аккаунт.</p>
<#else>
    <p style="color: red;">Ошибка подтверждения. Возможно, ссылка недействительна или аккаунт уже был активирован ранее.</p>
</#if>

<form action="/login" method="get">
    <button type="submit">Войти</button>
</form>

</body>
</html>