// tests/login.spec.js
const { test, expect } = require('@playwright/test');

test('login correcto', async ({ page }) => {

    // Entrar al login
    await page.goto('http://localhost:8080/vista/');

    // Ingresar al Usuario
    await page.fill('[id="loginForm:usuario"]', 'ADM1000');

    // Ingresar contraseña
    await page.fill('[id="loginForm:contrasena"]', '123');

    // Clic en boton para inicar sesion
    await page.click('text=Iniciar sesión');

    // Esperar cambio
    await page.waitForTimeout(3000);

    // Pruebas de Inicio de sesion - CP-01

    // OPCIÓN A: validar cambio de URL
    await expect(page).not.toHaveURL('http://localhost:8080/vista/home.xhtml');

    // OPCIÓN B: validar que ya no estás en login
    //await expect(page.locator('text=Inicio de sesion')).not.toBeVisible();

});