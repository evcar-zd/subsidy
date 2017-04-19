var gulp = require('gulp');
var changed = require('gulp-changed');
var path = require('path');
var debug = require('gulp-debug');

var paths = {};

paths.static = {
    src: 'src/main/resources/static/**/*',
    dst: 'build/resources/main/static/'
};

gulp.task('hotswap-static', function(){
    return gulp.src(paths.static.src)
        .pipe(changed(paths.static.dst))
        .pipe(debug({ title: 'hotswap detected : ' }))
        .pipe(gulp.dest(paths.static.dst));
});

gulp.task('watch', function(){
    return gulp.watch([paths.static.src], ['hotswap-static']);
});