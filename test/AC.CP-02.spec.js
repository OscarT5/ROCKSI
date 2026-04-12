const { test, expect } = require('@playwright/test');

test('Alta cliente sin sexo', async ({ page }) => {

    const logged = await login(page);

    if (!logged) return;

    await page.goto('http://localhost:8080/vista/clientes.xhtml');

    await page.click('button:has-text("Registrar Cliente")');

    await page.waitForSelector('[id$="nombreAlta"]');

    await page.fill('[id$="nombreAlta"]', 'Test');
    await page.fill('[id$="apellidoAlta"]', 'Usuario');
    await page.fill('[id$="telefonoAlta"]', '1234567890');

    await page.click('[id$="registrarBtn"]');

    const error = page.locator('.ui-messages-error');

    await expect(error).toBeVisible();
});