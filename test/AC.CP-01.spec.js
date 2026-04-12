const { test, expect } = require('@playwright/test');

test('Alta cliente correcta', async ({ page }) => {

    await page.goto('http://localhost:8080/vista/');

    // Esperar la carga
    await page.waitForLoadState('networkidle');
    await page.waitForSelector('[id$="usuario"]', { timeout: 30000 });

    // Login
    await page.fill('[id$="usuario"]', 'ADM1000');
    await page.fill('[id$="contrasena"]', '123');
    await page.click('input[value="Iniciar sesión"]');

    await page.waitForURL('**/home.xhtml', { timeout: 20000 });

    await page.goto('http://localhost:8080/vista/clientes.xhtml');

    await page.click('button:has-text("Registrar Cliente")');

    await page.waitForSelector('[id$="nombreAlta"]');

    await page.fill('[id$="nombreAlta"]', 'Juan');
    await page.fill('[id$="apellidoAlta"]', 'Perez');
    await page.fill('[id$="telefonoAlta"]', '1234567890');

    await page.click('[id$="sexoAlta_label"]');
    await page.click('li:has-text("Masculino")');

    await page.click('[id$="registrarBtn"]');

    await page.waitForURL('**/pagos.xhtml');

    await expect(page).toHaveURL(/pagos.xhtml/);
});