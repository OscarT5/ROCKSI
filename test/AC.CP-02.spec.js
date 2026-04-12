const { test, expect } = require('@playwright/test');

test('Alta cliente sin sexo (fallido)', async ({ page }) => {

    await page.goto('http://localhost:8080/vista/');

    await page.fill('[id$="usuario"]', 'ADM1000');
    await page.fill('[id$="contrasena"]', '123');
    await page.click('input[value="Iniciar sesión"]');

    await page.waitForURL('**/home.xhtml');

    await page.goto('http://localhost:8080/vista/clientes.xhtml');

    await page.click('button:has-text("Registrar Cliente")');

    await page.fill('[id$="nombreAlta"]', 'Test');
    await page.fill('[id$="apellidoAlta"]', 'Usuario');
    await page.fill('[id$="telefonoAlta"]', '1234567890');

    await page.click('[id$="registrarBtn"]');

    // Validar mensaje de error REAL
    const error = page.locator('.ui-messages-error');
    await error.waitFor();

    await expect(error).toContainText('sexo');
});