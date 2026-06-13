/* DZ Ink & Paper — auth & onboarding screens.
   Exposes window.DZInkAuth = { Splash, Onboarding1, Onboarding2, Onboarding3, LogIn, SignUp, ForgotPassword, Verification } */
(function () {
  const { Ic, pal, cardStyle, Frame, Chip, Label, IconBtn, Btn, Field, Dots, COVER, BOOKS } = window.DZInk;

  /* ---------- Splash — colophon emblem ---------- */
  function Splash({ t, mode = 'L' }) {
    const c = pal(t, mode);
    const rid = 'dz-ring-' + mode;
    return (
      <Frame t={t} c={c} h={640}>
        <div style={{ height: '100%', display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', padding: '0 40px 50px' }}>
          {/* press-stamp emblem: double hairline ring, ring of type, mark + monogram */}
          <div style={{ position: 'relative', width: 216, height: 216 }}>
            <svg width="216" height="216" viewBox="0 0 216 216" style={{ display: 'block', position: 'absolute', inset: 0 }}>
              <circle cx="108" cy="108" r="104" fill="none" stroke={c.line} strokeWidth="1"></circle>
              <circle cx="108" cy="108" r="99.5" fill="none" stroke={c.line} strokeWidth="1"></circle>
              <circle cx="108" cy="108" r="64" fill="none" stroke={c.line} strokeWidth="1"></circle>
              <path id={rid} d="M 108,26 A 82,82 0 1 1 107.99,26" fill="none"></path>
              <text fontSize="9" letterSpacing="0.32em" fill={c.muted} style={{ fontFamily: t.fontBody, fontWeight: 600 }}>
                <textPath href={'#' + rid} startOffset="0" textLength="515.2" lengthAdjust="spacing">{'EST. ON GOOD BOOKS · DZ · EST. ON GOOD BOOKS · DZ ·\u00a0'}</textPath>
              </text>
            </svg>
            <div style={{ position: 'absolute', inset: 0, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
              <Ic n="book_open" s={34} color={c.accent} />
              <div style={{ font: `${t.displayWeight} 38px/1 ${t.fontDisplay}`, color: c.ink, marginTop: 8, letterSpacing: '-0.01em' }}>DZ</div>
            </div>
          </div>
          {/* ornament rule */}
          <div style={{ display: 'flex', alignItems: 'center', gap: 12, marginTop: 34 }}>
            <span style={{ width: 36, height: 1, background: c.line }}></span>
            <span style={{ width: 6, height: 6, background: c.accent, transform: 'rotate(45deg)' }}></span>
            <span style={{ width: 36, height: 1, background: c.line }}></span>
          </div>
          <div style={{ font: `400 15px/1.5 ${t.fontAccent}`, color: c.inkSoft, fontStyle: 'italic', marginTop: 18 }}>A quieter way to read</div>
        </div>
        <div style={{ position: 'absolute', bottom: 28, left: 0, right: 0, display: 'flex', justifyContent: 'center' }}>
          <span style={{ font: `600 10px/1 ${t.fontBody}`, letterSpacing: '0.16em', textTransform: 'uppercase', color: c.muted }}>DZ for iOS · 3.2.1</span>
        </div>
      </Frame>
    );
  }

  /* ---------- Onboarding scaffold ---------- */
  function Onb({ t, c, hero, title, copy, at }) {
    return (
      <Frame t={t} c={c} h={760}>
        <div style={{ height: '100%', display: 'flex', flexDirection: 'column', padding: '0 26px 26px' }}>
          <div style={{ display: 'flex', justifyContent: 'flex-end', padding: '4px 0 0' }}>
            <span style={{ font: `600 12.5px/1 ${t.fontBody}`, color: c.muted }}>Skip</span>
          </div>
          <div style={{ flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>{hero}</div>
          <div style={{ font: `${t.displayWeight} 30px/1.15 ${t.fontDisplay}`, color: c.ink, textAlign: 'center' }}>{title}</div>
          <p style={{ font: `400 14px/1.65 ${t.fontBody}`, color: c.inkSoft, textAlign: 'center', margin: '14px 8px 0' }}>{copy}</p>
          <div style={{ display: 'flex', justifyContent: 'center', margin: '24px 0 22px' }}><Dots c={c} n={3} at={at} /></div>
          <Btn t={t} c={c}>{at === 2 ? 'Get started' : 'Continue'}</Btn>
        </div>
      </Frame>
    );
  }

  function Onboarding1({ t, mode = 'L' }) {
    const c = pal(t, mode);
    const hero = (
      <div style={{ position: 'relative', width: 230, height: 270 }}>
        <img src={BOOKS[2].c} alt="" style={{ position: 'absolute', left: 0, top: 30, width: 110, height: 162, borderRadius: t.cover, objectFit: 'cover', transform: 'rotate(-7deg)', boxShadow: '0 16px 30px rgba(20,18,12,0.22)' }} />
        <img src={BOOKS[0].c} alt="" style={{ position: 'absolute', left: 58, top: 8, width: 124, height: 184, borderRadius: t.cover, objectFit: 'cover', zIndex: 2, boxShadow: '0 20px 38px rgba(20,18,12,0.3)' }} />
        <img src={BOOKS[1].c} alt="" style={{ position: 'absolute', right: 0, top: 36, width: 110, height: 162, borderRadius: t.cover, objectFit: 'cover', transform: 'rotate(7deg)', boxShadow: '0 16px 30px rgba(20,18,12,0.22)' }} />
        <div style={{ position: 'absolute', bottom: 0, left: '50%', transform: 'translateX(-50%)', whiteSpace: 'nowrap', font: `400 13px/1 ${t.fontAccent}`, fontStyle: 'italic', color: c.muted }}>thousands of titles, one shelf</div>
      </div>
    );
    return <Onb t={t} c={c} hero={hero} at={0} title="Read beautifully" copy="A calm, carefully set reading experience — like a well-printed page, wherever you are." />;
  }

  function Onboarding2({ t, mode = 'L' }) {
    const c = pal(t, mode);
    const hero = (
      <div style={{ width: 250 }}>
        <div style={{ ...cardStyle(t, c), padding: 16 }}>
          <Label t={t} c={c}>My shelf</Label>
          <div style={{ display: 'flex', gap: 10, marginTop: 12 }}>
            {[BOOKS[4], BOOKS[3], BOOKS[1]].map((b, i) => <img key={i} src={b.c} alt="" style={{ width: 62, height: 92, borderRadius: t.cover, objectFit: 'cover', boxShadow: '0 6px 14px rgba(20,18,12,0.16)' }} />)}
          </div>
          <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginTop: 14, paddingTop: 12, borderTop: `1px solid ${c.line}` }}>
            <Ic n="add_collection" s={15} color={c.accent} />
            <span style={{ font: `600 12px/1 ${t.fontBody}`, color: c.accent }}>New collection</span>
          </div>
        </div>
        <div style={{ display: 'flex', gap: 7, marginTop: 14, justifyContent: 'center' }}>
          <Chip t={t} c={c} solid>To read</Chip><Chip t={t} c={c}>Finished</Chip><Chip t={t} c={c}>Loved</Chip>
        </div>
      </div>
    );
    return <Onb t={t} c={c} hero={hero} at={1} title="Build your shelf" copy="Collect what you read, what you loved, and what's next — in collections that feel like your own library." />;
  }

  function Onboarding3({ t, mode = 'L' }) {
    const c = pal(t, mode);
    const AVP = '../assets/avatars/';
    const hero = (
      <div style={{ width: 250, display: 'flex', flexDirection: 'column', gap: 12 }}>
        {[['profile_2.png', 'Patricia', 'is reading Olive, Again'], ['profile_3.png', 'Daniel', 'finished Bestiary'], ['img_maria_renzy.png', 'Maria', 'loved Mexican Gothic']].map((f, i) => (
          <div key={i} style={{ ...cardStyle(t, c), padding: '12px 14px', display: 'flex', alignItems: 'center', gap: 12, transform: `translateX(${[0, 16, 4][i]}px)` }}>
            <img src={AVP + f[0]} alt="" style={{ width: 36, height: 36, borderRadius: t.radiusSm, objectFit: 'cover' }} />
            <div style={{ font: `400 12.5px/1.35 ${t.fontBody}`, color: c.inkSoft }}><b style={{ color: c.ink, fontWeight: 700 }}>{f[1]}</b> {f[2]}</div>
          </div>
        ))}
      </div>
    );
    return <Onb t={t} c={c} hero={hero} at={2} title="Read together" copy="Follow friends, swap recommendations, and see what your circle is reading right now." />;
  }

  /* ---------- Log in ---------- */
  function Social({ t, c, icon, label }) {
    return (
      <button style={{ flex: 1, height: 50, borderRadius: t.radiusSm + 2, border: `1px solid ${c.line}`, background: c.surface, color: c.ink, font: `600 13px/1 ${t.fontBody}`, cursor: 'pointer', display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 9 }}>
        <Ic n={icon} s={17} color={icon === 'apple' ? c.ink : undefined} />{label}
      </button>
    );
  }
  const Divider = ({ t, c }) => (
    <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
      <span style={{ flex: 1, height: 1, background: c.line }} />
      <span style={{ font: `400 11.5px/1 ${t.fontBody}`, color: c.muted }}>or</span>
      <span style={{ flex: 1, height: 1, background: c.line }} />
    </div>
  );

  function LogIn({ t, mode = 'L' }) {
    const c = pal(t, mode);
    return (
      <Frame t={t} c={c} h={780}>
        <div style={{ height: '100%', display: 'flex', flexDirection: 'column', padding: '14px 26px 26px' }}>
          <Label t={t} c={c}>DZ · Sign in</Label>
          <div style={{ font: `${t.displayWeight} 32px/1.1 ${t.fontDisplay}`, color: c.ink, marginTop: 14 }}>Welcome back,<br />reader</div>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 12, marginTop: 28 }}>
            <Field t={t} c={c} icon="email" value="amelia@hartwell.co" focus />
            <Field t={t} c={c} icon="lock" value="••••••••••" trailing={<Ic n="visibility_off" s={17} color={c.muted} />} />
          </div>
          <div style={{ display: 'flex', justifyContent: 'flex-end', marginTop: 12 }}>
            <span style={{ font: `600 12.5px/1 ${t.fontBody}`, color: c.accent }}>Forgot password?</span>
          </div>
          <Btn t={t} c={c} style={{ marginTop: 22 }}>Sign in</Btn>
          <div style={{ margin: '24px 0' }}><Divider t={t} c={c} /></div>
          <div style={{ display: 'flex', gap: 10 }}>
            <Social t={t} c={c} icon="google" label="Google" />
            <Social t={t} c={c} icon="apple" label="Apple" />
          </div>
          <div style={{ flex: 1 }} />
          <div style={{ textAlign: 'center', font: `400 13px/1 ${t.fontBody}`, color: c.muted }}>
            New here? <b style={{ color: c.accent, fontWeight: 700 }}>Create account</b>
          </div>
        </div>
      </Frame>
    );
  }

  /* ---------- Sign up ---------- */
  function SignUp({ t, mode = 'L' }) {
    const c = pal(t, mode);
    return (
      <Frame t={t} c={c} h={820}>
        <div style={{ height: '100%', display: 'flex', flexDirection: 'column', padding: '14px 26px 26px' }}>
          <Label t={t} c={c}>DZ · Create account</Label>
          <div style={{ font: `${t.displayWeight} 32px/1.1 ${t.fontDisplay}`, color: c.ink, marginTop: 14 }}>Begin your<br />reading life</div>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 12, marginTop: 26 }}>
            <Field t={t} c={c} icon="user" value="Amelia Hartwell" />
            <Field t={t} c={c} icon="at" placeholder="Choose a username" focus />
            <Field t={t} c={c} icon="email" placeholder="Email address" />
            <Field t={t} c={c} icon="lock" placeholder="Password" trailing={<Ic n="visibility_on" s={17} color={c.muted} />} />
          </div>
          <p style={{ font: `400 12px/1.6 ${t.fontBody}`, color: c.muted, margin: '16px 2px 0' }}>
            By continuing you agree to DZ's <b style={{ color: c.inkSoft, fontWeight: 600 }}>Terms</b> and <b style={{ color: c.inkSoft, fontWeight: 600 }}>Privacy policy</b>.
          </p>
          <Btn t={t} c={c} style={{ marginTop: 18 }}>Create account</Btn>
          <div style={{ margin: '22px 0' }}><Divider t={t} c={c} /></div>
          <div style={{ display: 'flex', gap: 10 }}>
            <Social t={t} c={c} icon="google" label="Google" />
            <Social t={t} c={c} icon="apple" label="Apple" />
          </div>
          <div style={{ flex: 1 }} />
          <div style={{ textAlign: 'center', font: `400 13px/1 ${t.fontBody}`, color: c.muted }}>
            Already a reader? <b style={{ color: c.accent, fontWeight: 700 }}>Sign in</b>
          </div>
        </div>
      </Frame>
    );
  }

  /* ---------- Forgot password ---------- */
  function ForgotPassword({ t, mode = 'L' }) {
    const c = pal(t, mode);
    return (
      <Frame t={t} c={c} h={640}>
        <div style={{ height: '100%', display: 'flex', flexDirection: 'column', padding: '4px 26px 26px' }}>
          <IconBtn t={t} c={c} n="back" />
          <div style={{ font: `${t.displayWeight} 30px/1.15 ${t.fontDisplay}`, color: c.ink, marginTop: 30 }}>Forgot your password?</div>
          <p style={{ font: `400 14px/1.65 ${t.fontBody}`, color: c.inkSoft, margin: '14px 0 0' }}>
            No trouble at all. Tell us the email you signed up with and we'll send a reset code.
          </p>
          <div style={{ marginTop: 26 }}>
            <Field t={t} c={c} icon="email" value="amelia@hartwell.co" focus />
          </div>
          <Btn t={t} c={c} style={{ marginTop: 18 }}>Send reset code</Btn>
          <div style={{ flex: 1 }} />
          <div style={{ textAlign: 'center', font: `400 13px/1 ${t.fontBody}`, color: c.muted }}>
            Remembered it? <b style={{ color: c.accent, fontWeight: 700 }}>Back to sign in</b>
          </div>
        </div>
      </Frame>
    );
  }

  /* ---------- Verification ---------- */
  function Verification({ t, mode = 'L' }) {
    const c = pal(t, mode);
    const digits = ['7', '2', '4', ''];
    return (
      <Frame t={t} c={c} h={640}>
        <div style={{ height: '100%', display: 'flex', flexDirection: 'column', padding: '4px 26px 26px' }}>
          <IconBtn t={t} c={c} n="back" />
          <div style={{ font: `${t.displayWeight} 30px/1.15 ${t.fontDisplay}`, color: c.ink, marginTop: 30 }}>Check your inbox</div>
          <p style={{ font: `400 14px/1.65 ${t.fontBody}`, color: c.inkSoft, margin: '14px 0 0' }}>
            We sent a 4-digit code to <b style={{ color: c.ink, fontWeight: 700 }}>amelia@hartwell.co</b>. It expires in 10 minutes.
          </p>
          <div style={{ display: 'flex', gap: 12, marginTop: 30 }}>
            {digits.map((d, i) => (
              <div key={i} style={{ flex: 1, height: 64, borderRadius: t.radiusSm + 2, border: `1px solid ${i === 3 ? c.accent : c.line}`, background: c.surface, display: 'flex', alignItems: 'center', justifyContent: 'center', font: `${t.displayWeight} 26px/1 ${t.fontDisplay}`, color: c.ink, position: 'relative' }}>
                {d || <span style={{ width: 2, height: 26, background: c.accent }} />}
              </div>
            ))}
          </div>
          <Btn t={t} c={c} style={{ marginTop: 26 }}>Verify</Btn>
          <div style={{ flex: 1 }} />
          <div style={{ textAlign: 'center', font: `400 13px/1 ${t.fontBody}`, color: c.muted }}>
            Nothing arrived? <b style={{ color: c.accent, fontWeight: 700 }}>Resend code</b> <span style={{ color: c.muted }}>· 0:42</span>
          </div>
        </div>
      </Frame>
    );
  }

  window.DZInkAuth = { Splash, Onboarding1, Onboarding2, Onboarding3, LogIn, SignUp, ForgotPassword, Verification };
})();
