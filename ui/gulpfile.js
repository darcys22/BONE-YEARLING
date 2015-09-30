var gulp = require('gulp');
var gulpLoadPlugins = require('gulp-load-plugins');
var plugins = gulpLoadPlugins();
var del = require('del');
var vinylPaths = require('vinyl-paths');
var merge = require('merge-stream');
var notifier = require('node-notifier');

gulp.task('watch', function() {
  plugins.watch('public/**/*.html', function () {
    gulp.start('default');
  } );
  plugins.watch('public/**/*.css', function () {
    gulp.start('default');
  } );
  plugins.watch('public/**/*.js', function () {
    gulp.start('default');
  } );
});

gulp.task('notify', ['build'], function() {
  notifier.notify({ title: "Angular:", message : "Finished Build" });
});

gulp.task('copy', [], function() {
            gulp.src(['public/assets/images/**/*'], {base: 'public'})
            .pipe(gulp.dest('dist/'));
});


gulp.task('build', ['clean'], function () {
    return gulp.src( [ 'public/index.html' ], {base: 'public'})
        .pipe(plugins.if(isIndexHtml, plugins.usemin({
            html: [plugins.minifyHtml({empty: true})],
            js: [plugins.ngAnnotate(), plugins.stripDebug(), plugins.uglify(), plugins.rev()],
            vendorjs: [plugins.rev()],
            vendorcss: [plugins.minifyCss(), 'concat', plugins.rev()],
            css: [plugins.minifyCss(), 'concat', plugins.rev()],
            templateCache: [
                plugins.addSrc(['public/partials/**/*.html']),
                plugins.debug(),
                plugins.angularTemplatecache({
                    module: 'myApp',
                    root: 'partials/'
                }),
                'concat',
                plugins.rev()
            ]
        })))
        .pipe(gulp.dest('dist/'));

    function isIndexHtml (file) {
        return file.path.match('index\\.html$');
    }
});

gulp.task('clean', function() {
    return gulp.src('dist/*')
        .pipe(vinylPaths(del));
});

gulp.task('default', ['notify'], function() {
    gulp.start('copy');
});

