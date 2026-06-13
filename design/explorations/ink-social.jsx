/* DZ Ink & Paper — social layer screens.
   Exposes window.DZInkSocial = { Profile, Friend, FriendList, InviteFriends, NoFriends, Chat, Notifications } */
(function () {
  const { Ic, pal, cardStyle, Frame, Nav, Chip, Label, IconBtn, TopBar, Btn, Field, BookRow, AV, BOOKS, FRIENDS } = window.DZInk;

  const OnlineDot = ({ c, s = 11 }) => (
    <span style={{ position: 'absolute', bottom: -2, right: -2, width: s, height: s, borderRadius: 999, background: c.accent, border: `2px solid ${c.paper}` }} />
  );

  /* ---------- Profile (own) ---------- */
  function Profile({ t, mode = 'L' }) {
    const c = pal(t, mode);
    const menu = [
      ['stats', 'Reading goals', '21-day streak'],
      ['book', 'Collections', '3 shelves'],
      ['purchased', 'Purchases', '14 books'],
      ['premium', 'Membership', 'Free plan'],
      ['settings', 'Settings', ''],
    ];
    return (
      <Frame t={t} c={c} h={860}>
        <div style={{ height: '100%', paddingBottom: 64 }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '6px 22px 0' }}>
            <div style={{ font: `${t.displayWeight} 24px/1 ${t.fontDisplay}`, color: c.ink }}>You</div>
            <IconBtn t={t} c={c} n="bell" s={17} />
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: 16, padding: '20px 22px 0' }}>
            <img src={AV + 'profile_1.png'} alt="" style={{ width: 72, height: 72, borderRadius: t.radiusSm + 4, objectFit: 'cover', boxShadow: '0 10px 20px rgba(20,18,12,0.18)' }} />
            <div style={{ flex: 1, minWidth: 0 }}>
              <div style={{ font: `${t.displayWeight} 21px/1.15 ${t.fontDisplay}`, color: c.ink }}>Amelia Hartwell</div>
              <div style={{ font: `400 12.5px/1 ${t.fontBody}`, color: c.muted, marginTop: 6 }}>@amelia.reads · since 2024</div>
            </div>
            <span style={{ font: `600 12px/1 ${t.fontBody}`, color: c.accent }}>Edit</span>
          </div>

          <div style={{ display: 'flex', margin: '20px 22px 0', ...cardStyle(t, c), padding: '15px 0' }}>
            {[['Books', '48'], ['Friends', '12'], ['Streak', '21d']].map((m, i) => (
              <div key={i} style={{ flex: 1, textAlign: 'center', borderLeft: i ? `1px solid ${c.line}` : 'none' }}>
                <div style={{ font: `${t.displayWeight} 17px/1.1 ${t.fontDisplay}`, color: c.ink }}>{m[1]}</div>
                <div style={{ font: `500 10px/1 ${t.fontBody}`, letterSpacing: '0.08em', textTransform: 'uppercase', color: c.muted, marginTop: 5 }}>{m[0]}</div>
              </div>
            ))}
          </div>

          {/* friends preview */}
          <div style={{ display: 'flex', alignItems: 'center', gap: 12, padding: '20px 22px 0' }}>
            <div style={{ display: 'flex' }}>
              {FRIENDS.slice(0, 4).map((f, i) => (
                <img key={i} src={f.av} alt="" style={{ width: 32, height: 32, borderRadius: t.radiusSm - 2, objectFit: 'cover', marginLeft: i ? -9 : 0, border: `2px solid ${c.paper}`, position: 'relative', zIndex: 4 - i }} />
              ))}
            </div>
            <span style={{ flex: 1, font: `400 12.5px/1.3 ${t.fontBody}`, color: c.inkSoft }}><b style={{ fontWeight: 700, color: c.ink }}>12 friends</b> · 3 reading now</span>
            <span style={{ transform: 'rotate(180deg)', display: 'flex' }}><Ic n="back" s={14} color={c.muted} /></span>
          </div>

          <div style={{ margin: '20px 22px 0', ...cardStyle(t, c) }}>
            {menu.map((m, i) => (
              <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 13, padding: '14px 16px', borderTop: i ? `1px solid ${c.line}` : 'none' }}>
                <div style={{ width: 36, height: 36, borderRadius: t.radiusSm, background: c.alt, display: 'flex', alignItems: 'center', justifyContent: 'center' }}><Ic n={m[0]} s={16} color={c.accent} /></div>
                <span style={{ flex: 1, font: `600 13.5px/1 ${t.fontBody}`, color: c.ink }}>{m[1]}</span>
                {m[2] && <span style={{ font: `400 11.5px/1 ${t.fontBody}`, color: c.muted }}>{m[2]}</span>}
                <span style={{ transform: 'rotate(180deg)', display: 'flex' }}><Ic n="back" s={13} color={c.muted} /></span>
              </div>
            ))}
          </div>
        </div>
        <Nav t={t} c={c} active="you" />
      </Frame>
    );
  }

  /* ---------- Friend profile ---------- */
  function Friend({ t, mode = 'L' }) {
    const c = pal(t, mode);
    return (
      <Frame t={t} c={c} h={840}>
        <div style={{ height: '100%' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', padding: '4px 22px 0' }}>
            <IconBtn t={t} c={c} n="back" />
            <IconBtn t={t} c={c} n="three_vertical_dots" s={16} />
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', padding: '12px 26px 0' }}>
            <div style={{ position: 'relative' }}>
              <img src={AV + 'profile_2.png'} alt="" style={{ width: 92, height: 92, borderRadius: t.radiusSm + 6, objectFit: 'cover', boxShadow: '0 14px 28px rgba(20,18,12,0.22)' }} />
              <OnlineDot c={c} s={15} />
            </div>
            <div style={{ font: `${t.displayWeight} 24px/1.15 ${t.fontDisplay}`, color: c.ink, marginTop: 16 }}>Patricia Lane</div>
            <div style={{ font: `400 12.5px/1 ${t.fontBody}`, color: c.muted, marginTop: 7 }}>@patricia.reads · friends since March</div>
          </div>

          <div style={{ display: 'flex', margin: '20px 22px 0', ...cardStyle(t, c), padding: '15px 0' }}>
            {[['Books', '73'], ['Friends', '28'], ['In common', '9']].map((m, i) => (
              <div key={i} style={{ flex: 1, textAlign: 'center', borderLeft: i ? `1px solid ${c.line}` : 'none' }}>
                <div style={{ font: `${t.displayWeight} 17px/1.1 ${t.fontDisplay}`, color: c.ink }}>{m[1]}</div>
                <div style={{ font: `500 10px/1 ${t.fontBody}`, letterSpacing: '0.08em', textTransform: 'uppercase', color: c.muted, marginTop: 5 }}>{m[0]}</div>
              </div>
            ))}
          </div>

          <div style={{ display: 'flex', gap: 10, padding: '16px 22px 0' }}>
            <Btn t={t} c={c} h={46} kind="soft" style={{ flex: 1 }}><Ic n="done" s={13} color={c.accent} />Friends</Btn>
            <Btn t={t} c={c} h={46} style={{ flex: 1 }}><Ic n="chat" s={15} color={c.onAccent} />Message</Btn>
          </div>

          <div style={{ padding: '24px 22px 0' }}>
            <Label t={t} c={c}>Reading now</Label>
            <div style={{ ...cardStyle(t, c), padding: 14, marginTop: 12, display: 'flex', gap: 14, alignItems: 'center' }}>
              <img src={BOOKS[4].c} alt="" style={{ width: 46, height: 68, borderRadius: t.cover, objectFit: 'cover', boxShadow: '0 6px 14px rgba(20,18,12,0.18)' }} />
              <div style={{ flex: 1, minWidth: 0 }}>
                <div style={{ font: `${t.displayWeight} 15px/1.2 ${t.fontDisplay}`, color: c.ink }}>{BOOKS[4].t}</div>
                <div style={{ font: `400 11.5px/1.2 ${t.fontBody}`, color: c.muted, marginTop: 4 }}>{BOOKS[4].a}</div>
                <div style={{ height: 4, borderRadius: 999, background: c.alt, overflow: 'hidden', marginTop: 10, width: '80%' }}><div style={{ width: '44%', height: '100%', background: c.accent, borderRadius: 999 }} /></div>
              </div>
            </div>
          </div>

          <div style={{ padding: '22px 0 0' }}>
            <div style={{ display: 'flex', alignItems: 'baseline', justifyContent: 'space-between', padding: '0 22px', marginBottom: 13 }}>
              <div style={{ font: `${t.displayWeight} 17px/1 ${t.fontDisplay}`, color: c.ink }}>Patricia's shelf</div>
              <span style={{ font: `600 12px/1 ${t.fontBody}`, color: c.accent }}>See all</span>
            </div>
            <div style={{ display: 'flex', gap: 12, padding: '0 22px', overflow: 'hidden' }}>
              {[BOOKS[3], BOOKS[0], BOOKS[1], BOOKS[2]].map((b, i) => <img key={i} src={b.c} alt="" style={{ width: 72, height: 106, borderRadius: t.cover, objectFit: 'cover', flexShrink: 0, boxShadow: '0 6px 14px rgba(20,18,12,0.14)' }} />)}
            </div>
          </div>
        </div>
      </Frame>
    );
  }

  /* ---------- Friend list ---------- */
  function FriendList({ t, mode = 'L' }) {
    const c = pal(t, mode);
    return (
      <Frame t={t} c={c} h={840}>
        <div style={{ height: '100%' }}>
          <TopBar t={t} c={c} title="Friends" sub="12 friends · 3 online" right={<IconBtn t={t} c={c} n="plus" s={15} />} />
          <div style={{ padding: '4px 22px 0' }}>
            <Field t={t} c={c} icon="search" placeholder="Search friends" />
          </div>

          <div style={{ padding: '20px 22px 0' }}>
            <Label t={t} c={c}>Online now</Label>
            <div style={{ marginTop: 4 }}>
              {FRIENDS.filter(f => f.on).map((f, i) => (
                <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 13, padding: '12px 0', borderBottom: `1px solid ${c.line}` }}>
                  <div style={{ position: 'relative' }}>
                    <img src={f.av} alt="" style={{ width: 44, height: 44, borderRadius: t.radiusSm, objectFit: 'cover' }} />
                    <OnlineDot c={c} />
                  </div>
                  <div style={{ flex: 1, minWidth: 0 }}>
                    <div style={{ font: `700 13.5px/1 ${t.fontBody}`, color: c.ink }}>{f.n}</div>
                    <div style={{ font: `400 11.5px/1 ${t.fontBody}`, color: c.muted, marginTop: 5 }}>{f.h}</div>
                  </div>
                  <div style={{ width: 38, height: 38, borderRadius: t.radiusSm, background: c.accentSoft, display: 'flex', alignItems: 'center', justifyContent: 'center' }}><Ic n="chat" s={16} color={c.accent} /></div>
                </div>
              ))}
            </div>
          </div>

          <div style={{ padding: '18px 22px 0' }}>
            <Label t={t} c={c}>Everyone</Label>
            <div style={{ marginTop: 4 }}>
              {FRIENDS.filter(f => !f.on).map((f, i, arr) => (
                <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 13, padding: '12px 0', borderBottom: i < arr.length - 1 ? `1px solid ${c.line}` : 'none' }}>
                  <img src={f.av} alt="" style={{ width: 44, height: 44, borderRadius: t.radiusSm, objectFit: 'cover' }} />
                  <div style={{ flex: 1, minWidth: 0 }}>
                    <div style={{ font: `700 13.5px/1 ${t.fontBody}`, color: c.ink }}>{f.n}</div>
                    <div style={{ font: `400 11.5px/1 ${t.fontBody}`, color: c.muted, marginTop: 5 }}>{f.h}</div>
                  </div>
                  <div style={{ width: 38, height: 38, borderRadius: t.radiusSm, border: `1px solid ${c.line}`, display: 'flex', alignItems: 'center', justifyContent: 'center' }}><Ic n="chat" s={16} color={c.muted} /></div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </Frame>
    );
  }

  /* ---------- Invite friends ---------- */
  function InviteFriends({ t, mode = 'L' }) {
    const c = pal(t, mode);
    const contacts = [
      { n: 'Masaa Okafor', h: 'From contacts', av: AV + 'img_masaa.png' },
      { n: 'Raunak Purohit', h: 'From contacts', av: AV + 'img_raunak_purohit.png' },
      { n: 'Yza Barretto', h: 'Reads on DZ', av: AV + 'img_yza_barretto.png', on: true },
      { n: 'Neil Alvin', h: 'From contacts', av: AV + 'img_neil_alvin.png' },
    ];
    return (
      <Frame t={t} c={c} h={780}>
        <div style={{ height: '100%' }}>
          <TopBar t={t} c={c} title="Invite friends" />

          <div style={{ margin: '8px 22px 0', ...cardStyle(t, c), background: c.alt, border: 'none', padding: 18 }}>
            <Label t={t} c={c} style={{ color: c.accent }}>Reading is better shared</Label>
            <p style={{ font: `400 13px/1.6 ${t.fontBody}`, color: c.inkSoft, margin: '10px 0 0' }}>Invite a friend — you each get <b style={{ color: c.ink, fontWeight: 700 }}>100 coins</b> when they finish their first book.</p>
            <div style={{ display: 'flex', gap: 10, marginTop: 14 }}>
              <div style={{ flex: 1, height: 44, borderRadius: t.radiusSm, border: `1px dashed ${c.line}`, background: c.surface, display: 'flex', alignItems: 'center', padding: '0 14px', font: `600 12.5px/1 ${t.fontBody}`, color: c.inkSoft, letterSpacing: '0.04em' }}>dz.app/r/amelia</div>
              <Btn t={t} c={c} h={44} style={{ padding: '0 16px' }}><Ic n="share" s={14} color={c.onAccent} />Share</Btn>
            </div>
          </div>

          <div style={{ padding: '22px 22px 0' }}>
            <Label t={t} c={c}>Suggested</Label>
            <div style={{ marginTop: 4 }}>
              {contacts.map((f, i) => (
                <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 13, padding: '12px 0', borderBottom: i < contacts.length - 1 ? `1px solid ${c.line}` : 'none' }}>
                  <img src={f.av} alt="" style={{ width: 44, height: 44, borderRadius: t.radiusSm, objectFit: 'cover' }} />
                  <div style={{ flex: 1, minWidth: 0 }}>
                    <div style={{ font: `700 13.5px/1 ${t.fontBody}`, color: c.ink }}>{f.n}</div>
                    <div style={{ font: `400 11.5px/1 ${t.fontBody}`, color: f.on ? c.accent : c.muted, marginTop: 5 }}>{f.h}</div>
                  </div>
                  <span style={{ display: 'inline-flex', alignItems: 'center', height: 32, padding: '0 16px', borderRadius: 999, font: `700 12px/1 ${t.fontBody}`,
                    background: f.on ? c.accent : 'transparent', color: f.on ? c.onAccent : c.accent, border: f.on ? 'none' : `1px solid ${c.accent}` }}>
                    {f.on ? 'Add friend' : 'Invite'}
                  </span>
                </div>
              ))}
            </div>
          </div>
        </div>
      </Frame>
    );
  }

  /* ---------- No friends (empty) ---------- */
  function NoFriends({ t, mode = 'L' }) {
    const c = pal(t, mode);
    return (
      <Frame t={t} c={c} h={720}>
        <div style={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
          <TopBar t={t} c={c} title="Friends" />
          <div style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', padding: '0 44px 60px', textAlign: 'center' }}>
            <div style={{ position: 'relative', width: 96, height: 72 }}>
              <div style={{ position: 'absolute', left: 0, top: 12, width: 52, height: 52, borderRadius: t.radiusSm + 2, background: c.alt, transform: 'rotate(-8deg)' }} />
              <div style={{ position: 'absolute', right: 0, top: 12, width: 52, height: 52, borderRadius: t.radiusSm + 2, background: c.alt, transform: 'rotate(8deg)' }} />
              <div style={{ position: 'absolute', left: 22, top: 0, width: 52, height: 52, borderRadius: t.radiusSm + 2, background: c.accentSoft, display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 2, border: `2px solid ${c.paper}` }}>
                <Ic n="user" s={22} color={c.accent} />
              </div>
            </div>
            <div style={{ font: `${t.displayWeight} 24px/1.2 ${t.fontDisplay}`, color: c.ink, marginTop: 26 }}>No friends yet</div>
            <p style={{ font: `400 13.5px/1.65 ${t.fontBody}`, color: c.muted, margin: '12px 0 0' }}>
              Books are better with company. Find people you know, or invite someone to read alongside you.
            </p>
            <Btn t={t} c={c} style={{ marginTop: 26, alignSelf: 'stretch' }}>Find friends</Btn>
            <span style={{ font: `600 13px/1 ${t.fontBody}`, color: c.accent, marginTop: 18 }}>Invite from contacts</span>
          </div>
        </div>
      </Frame>
    );
  }

  /* ---------- Chat ---------- */
  function Chat({ t, mode = 'L' }) {
    const c = pal(t, mode);
    const Bubble = ({ me, children, time }) => (
      <div style={{ display: 'flex', flexDirection: 'column', alignItems: me ? 'flex-end' : 'flex-start' }}>
        <div style={{ maxWidth: '78%', padding: '11px 14px', borderRadius: t.radius, borderBottomRightRadius: me ? 4 : t.radius, borderBottomLeftRadius: me ? t.radius : 4,
          background: me ? c.accent : c.surface, border: me ? 'none' : `1px solid ${c.line}`, color: me ? c.onAccent : c.ink, font: `400 13.5px/1.55 ${t.fontBody}` }}>{children}</div>
        <span style={{ font: `400 10px/1 ${t.fontBody}`, color: c.muted, margin: '5px 4px 0' }}>{time}</span>
      </div>
    );
    return (
      <Frame t={t} c={c} h={840}>
        <div style={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 13, padding: '4px 22px 12px', borderBottom: `1px solid ${c.line}` }}>
            <IconBtn t={t} c={c} n="back" />
            <div style={{ position: 'relative' }}>
              <img src={AV + 'profile_2.png'} alt="" style={{ width: 42, height: 42, borderRadius: t.radiusSm, objectFit: 'cover' }} />
              <OnlineDot c={c} />
            </div>
            <div style={{ flex: 1 }}>
              <div style={{ font: `700 14px/1 ${t.fontBody}`, color: c.ink }}>Patricia Lane</div>
              <div style={{ font: `400 11px/1 ${t.fontBody}`, color: c.accent, marginTop: 4 }}>Online</div>
            </div>
            <IconBtn t={t} c={c} n="three_vertical_dots" s={16} />
          </div>

          <div style={{ flex: 1, padding: '18px 22px 0', display: 'flex', flexDirection: 'column', gap: 14, overflow: 'hidden' }}>
            <div style={{ alignSelf: 'center', font: `600 10px/1 ${t.fontBody}`, letterSpacing: '0.1em', textTransform: 'uppercase', color: c.muted }}>Today</div>
            <Bubble time="14:02">Finished Olive, Again last night — the last chapter undid me completely.</Bubble>
            <Bubble me time="14:05">I told you! Strout never raises her voice and it still lands harder than anything.</Bubble>
            <Bubble time="14:06">Okay, what's next then. You always know.</Bubble>
            {/* book share */}
            <div style={{ alignSelf: 'flex-end', maxWidth: '78%' }}>
              <div style={{ ...cardStyle(t, c), padding: 12, display: 'flex', gap: 12, alignItems: 'center' }}>
                <img src={BOOKS[2].c} alt="" style={{ width: 40, height: 58, borderRadius: t.cover - 1, objectFit: 'cover' }} />
                <div style={{ minWidth: 0 }}>
                  <div style={{ font: `${t.displayWeight} 13.5px/1.2 ${t.fontDisplay}`, color: c.ink }}>{BOOKS[2].t}</div>
                  <div style={{ font: `400 11px/1.2 ${t.fontBody}`, color: c.muted, marginTop: 4 }}>{BOOKS[2].a}</div>
                  <div style={{ font: `600 11px/1 ${t.fontBody}`, color: c.accent, marginTop: 7 }}>View book</div>
                </div>
              </div>
              <div style={{ textAlign: 'right', font: `400 10px/1 ${t.fontBody}`, color: c.muted, margin: '5px 4px 0' }}>14:07 · Read</div>
            </div>
          </div>

          <div style={{ padding: '12px 22px 20px', display: 'flex', gap: 10, alignItems: 'center', borderTop: `1px solid ${c.line}` }}>
            <div style={{ flex: 1, height: 46, borderRadius: 999, border: `1px solid ${c.line}`, background: c.surface, display: 'flex', alignItems: 'center', padding: '0 16px', font: `400 13px/1 ${t.fontBody}`, color: c.muted }}>Write a message…</div>
            <div style={{ width: 46, height: 46, borderRadius: 999, background: c.accent, display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0 }}>
              <Ic n="send" s={18} color={c.onAccent} />
            </div>
          </div>
        </div>
      </Frame>
    );
  }

  /* ---------- Notifications ---------- */
  function Notifications({ t, mode = 'L' }) {
    const c = pal(t, mode);
    const items = [
      { av: 'profile_2.png', txt: ['Patricia Lane', ' sent you a book — ', 'Red at the Bone'], time: '2m', unread: true, cover: BOOKS[2].c },
      { av: 'img_maria_renzy.png', txt: ['Maria Renzy', ' accepted your friend request'], time: '1h', unread: true },
      { av: 'profile_3.png', txt: ['Daniel Moreau', ' reviewed ', 'Bestiary', ' · 4★'], time: '3h' },
      { ic: 'stats', txt: ['Goal reached', ' — 30 minutes today. 21-day streak.'], time: '9h' },
      { ic: 'tag', txt: ['Price drop', ' — ', 'The Archer', ' is now $5.99'], time: '1d', cover: BOOKS[1].c },
    ];
    return (
      <Frame t={t} c={c} h={840}>
        <div style={{ height: '100%' }}>
          <TopBar t={t} c={c} title="Notifications" right={<span style={{ font: `600 12px/1 ${t.fontBody}`, color: c.accent }}>Mark all read</span>} />
          <div style={{ display: 'flex', gap: 8, padding: '2px 22px 8px' }}>
            <Chip t={t} c={c} solid>All</Chip><Chip t={t} c={c}>Friends</Chip><Chip t={t} c={c}>Store</Chip>
          </div>
          <div style={{ padding: '0 22px' }}>
            {items.map((n, i) => (
              <div key={i} style={{ display: 'flex', gap: 13, alignItems: 'center', padding: '14px 0', borderBottom: i < items.length - 1 ? `1px solid ${c.line}` : 'none' }}>
                {n.av
                  ? <img src={AV + n.av} alt="" style={{ width: 42, height: 42, borderRadius: t.radiusSm, objectFit: 'cover', flexShrink: 0 }} />
                  : <div style={{ width: 42, height: 42, borderRadius: t.radiusSm, background: c.alt, display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0 }}><Ic n={n.ic} s={17} color={c.accent} /></div>}
                <div style={{ flex: 1, minWidth: 0 }}>
                  <div style={{ font: `400 13px/1.45 ${t.fontBody}`, color: c.inkSoft }}>
                    {n.txt.map((s, j) => j % 2 === 0 ? <b key={j} style={{ fontWeight: 700, color: c.ink }}>{s}</b> : <span key={j}>{s}</span>)}
                  </div>
                  <div style={{ font: `400 10.5px/1 ${t.fontBody}`, color: c.muted, marginTop: 5 }}>{n.time} ago</div>
                </div>
                {n.cover && <img src={n.cover} alt="" style={{ width: 30, height: 44, borderRadius: t.cover - 2, objectFit: 'cover', flexShrink: 0 }} />}
                {n.unread && <span style={{ width: 7, height: 7, borderRadius: 999, background: c.accent, flexShrink: 0 }} />}
              </div>
            ))}
          </div>
        </div>
      </Frame>
    );
  }

  window.DZInkSocial = { Profile, Friend, FriendList, InviteFriends, NoFriends, Chat, Notifications };
})();
