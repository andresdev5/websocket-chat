import { defineConfig } from 'cypress'

export default defineConfig({
    e2e: {
        specPattern: 'tests/e2e/**/*.cy.ts',
    },


    component: {
        devServer: {
            framework: 'angular',
            bundler: 'webpack',
        },
        specPattern: 'tests/components/**/*.cy.ts'
    }
})
