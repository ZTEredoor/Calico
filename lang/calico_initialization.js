// calico system library

M.Configuration = function() {
    this.configure = {
        port                : 8080,
        template_directory  : "./template",
        target_directory    : "./target",
        resource_directory  : "./",
        root_page           : "/index.html",
    };
};

M.Configuration.prototype.value_of_string = function(name) {
    if (!this.configure[name]) {
        return null;
    }
    return "" + this.configure[name];
};

M.Configuration.prototype.value_of_integer = function(name) {
    if (!this.configure[name]) {
        return 0;
    }
    return parseInt(this.configure[name]);
}