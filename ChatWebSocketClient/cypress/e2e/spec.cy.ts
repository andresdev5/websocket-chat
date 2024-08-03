describe('My First Test', () => {
    it('Check Home page', () => {
        cy.visit('/')
        cy.contains('Cursos')
    });
});
