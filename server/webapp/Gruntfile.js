var IN_PREFIX = 'process/webapp/';
var OUT_PREFIX = 'static/webapp/';

var JS_IN_PREFIX = IN_PREFIX + 'js/';
var JS_OUT_PREFIX = OUT_PREFIX + 'js/';

var SCSS_IN_PREFIX = IN_PREFIX + 'scss/';
var CSS_OUT_PREFIX = OUT_PREFIX + 'css/';

module.exports = function(grunt) {
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    sass: {
    	options: {
    	  style: "compressed"
    	},
    	dist: {
    		files: [{
    			'src': [SCSS_IN_PREFIX + 'style.scss'],
    			'dest': CSS_OUT_PREFIX + 'style.css'
    		},
    		{
    			'src': [SCSS_IN_PREFIX + 'style.scss'],
    			'dest': CSS_OUT_PREFIX + 'pad.css'
    		},
    		{
    			'src': [SCSS_IN_PREFIX + 'application.scss'],
    			'dest': CSS_OUT_PREFIX + 'application.css'
    		}]
    	}
    },
    concat: {
        options: {
          separator: ';'
        },
        dist: {
          files: [{
          	// Application
          	'src': [
          		JS_IN_PREFIX + 'libs/angular/angular.js',
          		JS_IN_PREFIX + 'libs/angular/angular-route.js',
          		JS_IN_PREFIX + 'application/application.js',
          		JS_IN_PREFIX + 'application/*/**/*.js'
          	],
          	'dest': JS_OUT_PREFIX + 'application.js',
          },
          {
          	'src': [
          		JS_IN_PREFIX + 'libs/jquery-2.0.3.js',
          		JS_IN_PREFIX + 'libs/base64v1_0.js',
          		JS_IN_PREFIX + 'libs/diff-match-patch/diff_match_patch_uncompressed.js',
          		JS_IN_PREFIX + 'libs/highlight/highlight.pack.js',
          		JS_IN_PREFIX + 'pad.js'
      		],
          	'dest': JS_OUT_PREFIX + 'pad.js',
          }]
        }
    },
    uglify: {
      options: {
        banner: '/*! <%= pkg.name %> <%= grunt.template.today("dd-mm-yyyy") %> */\n'
      },
      dist: {
        files: [{
        	'src': [JS_OUT_PREFIX + 'application.js'],
        	'dest': JS_OUT_PREFIX + 'application.min.js'
        },
        {
        	'src': [JS_OUT_PREFIX + 'pad.js'],
        	'dest': JS_OUT_PREFIX + 'pad.min.js'
        }]
      }
    },
    watch: {
	  css: {
	  	files: 'process/**/*.scss',
	  	tasks: ['sass']
	  },
	  concat: {
	  	files: 'process/**/*.js',
	  	tasks: ['concat']
	  }
    }
  });
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-sass');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-concat');

  grunt.registerTask('default', ['concat', 'uglify', 'sass']);
};

