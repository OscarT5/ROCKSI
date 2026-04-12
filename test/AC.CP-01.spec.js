const { test, expect } = require('@playwright/test');

test('Alta cliente correcta', async ({ page }) => {

    const logged = await login(page);

    if (!logged) return;

    await page.goto('http://localhost:8080/vista/clientes.xhtml');

    await page.click('button:has-text("Registrar Cliente")');

    await page.waitForSelector('[id$="nombreAlta"]');

    await page.fill('[id$="nombreAlta"]', 'Juan');
    await page.fill('[id$="apellidoAlta"]', 'Perez');
    await page.fill('[id$="telefonoAlta"]', '1234567890');
    await page.fill('[id$="segundoTelefonoAlta"]', '0987654321');

    await page.click('[id$="sexoAlta_label"]');
    await page.click('li:has-text("Masculino")');

    await page.click('[id$="registrarBtn"]');

    await page.waitForURL('**/pagos.xhtml');

    await expect(page).toHaveURL(/pagos.xhtml/);
});