const { test, expect } = require('@playwright/test');

test('login ROCKSI', async ({ page }) => {

    if (process.env.CI) {
        test.skip();
    }

    await page.goto('http://localhost:8080/vista/');

    await page.fill('[id="loginForm:usuario"]', 'ADM1000');
    await page.fill('[id="loginForm:contrasena"]', '1234');
    await page.click('text=Iniciar sesión');

    await page.waitForTimeout(3000);

    await expect(page).not.toHaveURL('http://localhost:8080/vista/home.xhtml');
});