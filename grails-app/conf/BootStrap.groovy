class BootStrap {
    def crmAccountService
    def crmSecurityService
    def crmContentService

    def init = { servletContext ->
        def admin = crmSecurityService.createUser([username: "admin", password: "admin",
            email: "firstname.lastname@email.com", name: "Site Admin", enabled: true]) 

        crmSecurityService.addPermissionAlias("permission.all", ["*:*"])

        crmSecurityService.runAs(admin.username) {
            def account = crmAccountService.createAccount([status: "active"], [:]) 
            def tenant = crmSecurityService.createTenant(account, "Website") 
            crmSecurityService.runAs(admin.username, tenant.id) {
                crmSecurityService.addPermissionToUser("permission.all")

                def root = crmContentService.createFolder(null, "website", "Web site content", "", "") 
                def images = crmContentService.createFolder(root, "images", "Web site images", "", "")
                def pages = crmContentService.createFolder(root, "pages", "Web pages", "", "")
                crmContentService.createResource("""
                <h1>Welcome!</h1>
                <p>This app created by refer to the guide at http://gr8crm.github.io/tutorials/gr8crm-web/index.html</p>
                """, "index.html", pages)                
            }
        }        	
    }
    def destroy = {
    }
}
