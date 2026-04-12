const { test, expect } = require('@playwright/test');

test('login ROCKSI incorrecto', async ({ page }) => {

    await page.goto('http://localhost:8080/vista/', {
        waitUntil: 'networkidle'
    });

    await page.waitForSelector('[id$="usuario"]', { timeout: 20000 });

    await page.fill('[id$="usuario"]', 'ADM1000');
    await page.fill('[id$="contrasena"]', '0000');

    await page.click('text=Iniciar sesión');

    // Validar que no cambia a home
    await page.waitForTimeout(3000);

    await expect(page).not.toHaveURL(/home.xhtml/);

    // Validar mensaje de error
    await expect(page.locator('.ui-messages')).toBeVisible();
});