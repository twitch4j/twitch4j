module.exports = {
  preset: 'angular', // default preset angular
  plugins: [
    ['@semantic-release/commit-analyzer', {
      releaseRules: [
        { type: 'feat', release: 'minor' },
        { type: 'feature', release: 'minor' },
        { type: 'fix', release: 'minor' },
        { type: 'bugfix', release: 'minor' },
        { type: 'deps', release: 'patch' },
        { type: 'build', release: 'patch' },
        { type: 'chore', release: 'patch' },
        { type: 'pref', release: 'minor' },
        { type: 'docs', scope: 'README', release: 'patch' },
        { type: 'refactor', release: 'major' },
        { type: 'style', release: 'patch' }
      ],
      parserOpts: {
        noteKeywords: ['BREAKING CHANGE', 'BREAKING CHANGES']
      }
    }],
    '@semantic-release/release-notes-generator',
    'gradle-semantic-release-plugin',
    ['@semantic-release/git', {
  		assets: ['gradle.properties'],
		message: 'release: ${nextRelease.version}\n\n${nextRelease.notes}'
	}],
    ['@semantic-release/github', {
      assets: [{ path: '**/build/libs/**-shaded.jar' }]
    }],
    ['semantic-release-slack-bot', {
      notifyOnFail: false // No notification on failure
    }]
  ]
}
