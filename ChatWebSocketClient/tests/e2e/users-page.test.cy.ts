import { fixturesApi } from '../utils/fixtures';

describe('Users Page', () => {
    beforeEach(() => {
        cy.intercept('GET', 'http://localhost:8001/users', { body: fixturesApi.users.getAll() }).as('getUsers')
        cy.intercept('GET', 'http://localhost:8001/users/1', { body: fixturesApi.users.get() }).as('getUser')
    });

    it('Check Users page', () => {
        cy.visit('/users');
        cy.contains('Usuarios');
    });

    it('Should Edit User', () => {
        cy.visit('/users/edit/1');
        cy.wait('@getUser');
        cy.get('input[name="name"]').should('have.value', 'John Doe');
        cy.get('input[type="email"]').should('have.value', 'johndoe@gmail.com');
        cy.get('input[type="password"]').should('have.value', '12345');
    });

    it('Should Add New User', () => {
        const users = [
            ...fixturesApi.users.getAll(),
            { id: 15, name: 'Jane Doe', email: 'janedoe@gmail.com', password: '12345' }
        ];

        cy.intercept('POST', 'http://localhost:8001/users', { statusCode: 201 }).as('addUser');

        cy.visit('/users/add');
        cy.get('input[name="name"]').type('Jane Doe');
        cy.get('input[type="email"]').type('janedoe@gmail.com');
        cy.get('input[type="password"]').type('12345');

        cy.intercept('GET', 'http://localhost:8001/users', { body: users }).as('getUsers');

        cy.get('button[type="submit"]').click();

        cy.get('table tbody tr').should('have.length', 4);
        cy.get('table tbody tr:last-child td:first-child').should('contain.text', '15');
        cy.get('table tbody tr:last-child td:nth-child(2)').should('contain.text', 'Jane Doe');
        cy.get('table tbody tr:last-child td:nth-child(3)').should('contain.text', 'janedoe@gmail.com');
        cy.get('table tbody tr:last-child td:nth-child(4) span').should('contain.text', '****************');
    });

    it('Should Delete User', () => {
        const users = fixturesApi.users.getAll().filter(user => user.id !== 1);

        cy.intercept('DELETE', 'http://localhost:8001/users/1', { statusCode: 200 }).as('deleteUser');

        cy.visit('/users');
        cy.wait('@getUsers');
        cy.get('table tbody tr').should('have.length', 3);
        cy.wait(100);
        cy.get('table tbody tr:first-child td:last-child button').click();
        cy.wait(100);
        cy.get('.p-menu-list li:nth-child(2) a').click();
        cy.wait(100);
        cy.get('.p-dialog').should('exist');

        cy.intercept('GET', 'http://localhost:8001/users', { body: users }).as('getUsers');
        cy.get('.p-confirm-dialog-accept').click();
        cy.wait('@deleteUser');
        cy.get('table tbody tr').should('have.length', 2);
    });

    it('Should Show User Password', () => {
        cy.visit('/users');
        cy.wait('@getUsers');
        cy.get('table tbody tr:first-child td:nth-child(4) span').should('contain.text', '****************');
        cy.get('table tbody tr:first-child td:nth-child(4) button').click();
        cy.get('table tbody tr:first-child td:nth-child(4) span').should('contain.text', '12345');
    });
});
